package org.jaguar.modules.numbering.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.modules.numbering.mapper.RuleMapper;
import org.jaguar.modules.numbering.model.Rule;
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
    public Rule createOrUpdate(Rule rule) {
        Rule byName = this.getByName(rule.getName());
        Assert.duplicate(byName, rule, "编号规则名称");
        return this.saveOrUpdate(rule);
    }
}
