package org.jaguar.modules.numbering.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.numbering.mapper.RuleItemMapper;
import org.jaguar.modules.numbering.model.RuleItem;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 编号规则条目 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Service
public class RuleItemService extends BaseService<RuleItem, RuleItemMapper> {

    /**
     * 根据规则ID查询条目列表
     */
    public List<RuleItem> queryListByRuleIdOrderBySortNo(Long ruleId) {
        return this.list(JaguarLambdaQueryWrapper.<RuleItem>newInstance()
                .eq(RuleItem::getRuleId, ruleId)
                .orderByAsc(RuleItem::getSortNo));
    }

}
