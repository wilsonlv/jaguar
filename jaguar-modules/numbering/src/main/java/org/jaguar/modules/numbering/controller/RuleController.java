package org.jaguar.modules.numbering.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.numbering.mapper.RuleMapper;
import org.jaguar.modules.numbering.model.Rule;
import org.jaguar.modules.numbering.service.RuleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


/**
 * <p>
 * 编号规则表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Validated
@RestController
@RequestMapping("/numbering/rule")
@Api(tags = "编号规则表管理")
public class RuleController extends BaseController<Rule, RuleMapper, RuleService> {

    @ApiOperation(value = "查询编号规则表")
    @GetMapping(value = "/page")
    public JsonResult<Page<Rule>> page(
            @ApiIgnore Page<Rule> page,
            @ApiParam(value = "模糊编号规则名称") @RequestParam(required = false) String fuzzyName) {

        LambdaQueryWrapper<Rule> wrapper = JaguarLambdaQueryWrapper.<Rule>newInstance()
                .eq(Rule::getName, fuzzyName);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "编号规则表详情")
    @GetMapping(value = "/{id}")
    public JsonResult<Rule> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "更新编号规则")
    @PostMapping
    public JsonResult<Rule> update(@RequestBody @Valid Rule entity) {
        synchronized (this) {
            entity = service.createOrUpdate(entity);
        }
        return success(entity);
    }

    @ApiOperation(value = "删除编号规则表")
    @DeleteMapping(value = "/{id}")
    public JsonResult<?> del(@PathVariable Long id) {
        return super.delete(id);
    }

}