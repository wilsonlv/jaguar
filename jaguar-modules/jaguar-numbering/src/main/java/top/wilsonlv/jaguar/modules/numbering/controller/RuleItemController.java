package top.wilsonlv.jaguar.modules.numbering.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.modules.numbering.mapper.RuleItemMapper;
import top.wilsonlv.jaguar.modules.numbering.model.RuleItem;
import top.wilsonlv.jaguar.modules.numbering.service.RuleItemService;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "编号规则条目")
public class RuleItemController extends BaseController<RuleItem, RuleItemMapper, RuleItemService> {

    @ApiOperation(value = "查询编号规则条目")
    @PreAuthorize("hasAuthority('编号规则管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<RuleItem>> page(
            @ApiIgnore Page<RuleItem> page,
            @ApiParam(value = "编号规则ID", required = true) @RequestParam @NotNull Long ruleId) {

        LambdaQueryWrapper<RuleItem> wrapper = JaguarLambdaQueryWrapper.<RuleItem>newInstance()
                .eq(RuleItem::getRuleId, ruleId)
                .orderByAsc(RuleItem::getSortNo);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "编号规则条目详情")
    @PreAuthorize("hasAuthority('编号规则管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<RuleItem> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "更新编号规则条目")
    @PreAuthorize("hasAuthority('编号规则管理')")
    @PostMapping(value = "/update")
    public JsonResult<Void> update(@RequestBody @Valid RuleItem ruleItem) {
        return super.saveOrUpdate(ruleItem);
    }

    @ApiOperation(value = "删除编号规则条目")
    @PreAuthorize("hasAuthority('编号规则管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<?> del(@PathVariable Long id) {
        return super.delete(id);
    }

}