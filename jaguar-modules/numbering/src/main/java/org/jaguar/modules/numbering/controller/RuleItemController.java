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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 编号规则条目  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Validated
@RestController
@RequestMapping("/numbering/rule_item")
@Api(value = "编号规则条目")
public class RuleItemController extends AbstractController<RuleItem, RuleItemMapper, RuleItemService> {

    @ApiOperation(value = "查询编号规则条目")
    @RequiresPermissions("编号规则条目表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<RuleItem>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<RuleItem> page,
            @ApiParam(value = "编号规则ID", required = true) @RequestParam @NotNull Long ruleId) {

        LambdaQueryWrapper<RuleItem> wrapper = JaguarLambdaQueryWrapper.<RuleItem>newInstance()
                .eq(RuleItem::getRuleId, ruleId)
                .orderByAsc(RuleItem::getSortNo);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "编号规则条目详情")
    @RequiresPermissions("编号规则条目表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<RuleItem>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "更新编号规则条目")
    @RequiresPermissions("编号规则条目表:新增编辑")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<RuleItem>> update(@RequestBody @Valid RuleItem ruleItem) {
        return super.saveOrUpdate(ruleItem);
    }

    @ApiOperation(value = "删除编号规则条目")
    @RequiresPermissions("编号规则条目表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}