package top.wilsonlv.jaguar.modules.numbering.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.aviator.ExpressionUtil;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.modules.numbering.mapper.RuleSerialMapper;
import top.wilsonlv.jaguar.modules.numbering.model.Rule;
import top.wilsonlv.jaguar.modules.numbering.model.RuleItem;
import top.wilsonlv.jaguar.modules.numbering.model.RuleSerial;
import top.wilsonlv.jaguar.modules.numbering.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 编号规则序列 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Service
public class RuleSerialService extends BaseService<RuleSerial, RuleSerialMapper> {

    @Autowired
    private RuleService ruleService;
    @Autowired
    private RuleItemService ruleItemService;

    /**
     * 根据规则ID生成规则实例
     *
     * @param level 流水等级
     */
    @Transactional
    public String generateInstanceByRuleId(Long id, long level, Map<String, Object> sqlParams) {
        Rule rule = ruleService.getById(id);
        return generateInstance(rule, level, sqlParams);
    }

    /**
     * 根据名称生成规则实例
     *
     * @param level 流水等级
     */
    @Transactional
    public String generateInstanceByRuleName(String name, long level, Map<String, Object> sqlParams) {
        Rule rule = ruleService.getByName(name);
        if (rule == null) {
            throw new CheckedException("无效的规则名称！");
        }

        return generateInstance(rule, level, sqlParams);
    }

    /**
     * 根据名称生成规则实例，使用json串作为sql参数
     *
     * @param level 流水等级
     */
    @Transactional
    public String generateInstanceByRuleNameWithJsonParams(String name, long level, String sqlParams) {
        Rule rule = ruleService.getByName(name);
        if (rule == null) {
            throw new CheckedException("无效的规则名称！");
        }

        return generateInstance(rule, level, JSONObject.parseObject(sqlParams));
    }

    /**
     * 生成规则实例
     *
     * @param rule  编号规则
     * @param level 流水等级
     */
    @Transactional
    public String generateInstance(Rule rule, Long level, Map<String, Object> params) {
        List<RuleItem> ruleItems = ruleItemService.queryListByRuleIdOrderBySortNo(rule.getId());

        StringBuilder pattern = new StringBuilder();
        StringBuilder instance = new StringBuilder();
        for (RuleItem item : ruleItems) {
            switch (item.getNumberingRuleItemType()) {
                case FIXED: {
                    pattern.append(item.getName());
                    instance.append(item.getName());
                    continue;
                }
                case EXPRESSION: {
                    String result = String.valueOf(ExpressionUtil.execute(item.getName(), params));
                    if (item.getEffect()) {
                        pattern.append(result);
                    }

                    if (item.getShow()) {
                        instance.append(result);
                    }
                    continue;
                }
                case DATETIME: {
                    String format = DateTimeFormatter.ofPattern(item.getName()).format(LocalDateTime.now());
                    if (item.getEffect()) {
                        pattern.append(format);
                    }

                    if (item.getShow()) {
                        instance.append(format);
                    }
                    continue;
                }
                case SQL_QUERY: {
                    if (params != null) {
                        for (String key : params.keySet()) {
                            Object value = params.get(key);
                            if (value instanceof String) {
                                value = '\'' + (String) value + '\'';
                            }

                            String keyExpression = ExpressionUtil.DEFAULT_EXPRESSION_PRE + key + ExpressionUtil.DEFAULT_EXPRESSION_SUF;
                            item.setName(item.getName().replace(keyExpression, String.valueOf(value)));
                        }
                    }

                    List<LinkedHashMap<String, Object>> results = mapper.execute(item.getName());
                    if (results.size() != 1) {
                        throw new CheckedException("规则条目【ID：" + item.getId() + "】，SQL结果集错误【数量：" + results.size() + "】");
                    }
                    LinkedHashMap<String, Object> result = results.get(0);
                    if (result.size() != 1) {
                        throw new CheckedException("规则条目【ID：" + item.getId() + "】，SQL结果集错误【列数：" + result.size() + "】");
                    }

                    String value = String.valueOf(result.values().toArray()[0]);
                    if (item.getEffect()) {
                        pattern.append(value);
                    }

                    if (item.getShow()) {
                        instance.append(value);
                    }
                    continue;
                }
                case SERIAL_NUMBER: {
                    boolean newCreate = false;

                    String patternStr = pattern.toString();
                    LambdaQueryWrapper<RuleSerial> wrapper = new JaguarLambdaQueryWrapper<RuleSerial>()
                            .eq(RuleSerial::getRuleId, rule.getId())
                            .eq(RuleSerial::getRuleItemId, item.getId())
                            .eq(RuleSerial::getParttern, patternStr);
                    RuleSerial ruleSerial = this.unique(wrapper);
                    if (ruleSerial == null) {
                        //匹配流水号序列格式，如果没有，则创建
                        ruleSerial = new RuleSerial();
                        ruleSerial.setRuleId(rule.getId());
                        ruleSerial.setRuleItemId(item.getId());
                        ruleSerial.setParttern(patternStr);
                        ruleSerial.setSerialNumber(item.getName());
                        this.insert(ruleSerial);

                        newCreate = true;
                    }

                    String serialNumber;

                    //判断需要改变的流水序列等级
                    if (item.getLevel().equals(level.intValue())) {
                        //如果等级匹配，则增长
                        if (newCreate) {
                            serialNumber = ruleSerial.getSerialNumber();
                        } else {
                            serialNumber = NumberUtil.increase(ruleSerial.getSerialNumber());

                            ruleSerial.setSerialNumber(serialNumber);
                            this.updateById(ruleSerial);
                        }
                    } else {//否则使用当前序号
                        serialNumber = ruleSerial.getSerialNumber();
                    }

                    pattern.append(serialNumber);
                    instance.append(serialNumber);
                    continue;
                }
                default:
                    throw new CheckedException("编号规则条目类型错误【" + item.getNumberingRuleItemType() + "】");
            }
        }

        return instance.toString();
    }

}
