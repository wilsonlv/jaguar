package top.wilsonlv.jaguar.modules.numbering.service;

import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.modules.numbering.mapper.RuleMapper;
import top.wilsonlv.jaguar.modules.numbering.model.Rule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 编号规则表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Service
public class RuleService extends BaseService<Rule, RuleMapper> {

    public Rule getByName(String name) {
        return this.unique(JaguarLambdaQueryWrapper.<Rule>newInstance()
                .eq(Rule::getName, name));
    }

    /**
     * 新增或编辑编号规则
     */
    @Transactional
    public void createOrUpdate(Rule rule) {
        Rule byName = this.getByName(rule.getName());
        Assert.duplicate(byName, rule, "编号规则名称");
        this.saveOrUpdate(rule);
    }
}
