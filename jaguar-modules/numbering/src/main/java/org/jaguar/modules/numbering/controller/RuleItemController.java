package org.jaguar.modules.numbering.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.numbering.mapper.RuleItemMapper;
import org.jaguar.modules.numbering.model.RuleItem;
import org.jaguar.modules.numbering.service.RuleItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * <p>
 * 编号规则条目  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@RestController
@Api(value = "编号规则条目管理")
@RequestMapping("/numbering/rule_item")
public class RuleItemController extends AbstractController<RuleItem, RuleItemMapper, RuleItemService> {

    @ApiOperation(value = "查询编号规则条目")
    @RequiresPermissions("numbering_rule_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<RuleItem>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<RuleItem> page,
            @ApiParam(value = "编号规则ID") Long ruleId) {

        LambdaQueryWrapper<RuleItem> wrapper = JaguarLambdaQueryWrapper.<RuleItem>newInstance()
                .eq(RuleItem::getRuleId, ruleId)
                .orderByAsc(RuleItem::getSortNo);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "编号规则条目详情")
    @RequiresPermissions("numbering_rule_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<RuleItem>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "更新编号规则条目")
    @RequiresPermissions("numbering_rule_update")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<RuleItem>> update(@RequestBody RuleItem ruleItem) {
        return super.saveOrUpdate(ruleItem);
    }

    @ApiOperation(value = "删除编号规则条目")
    @RequiresPermissions("numbering_rule_del")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}