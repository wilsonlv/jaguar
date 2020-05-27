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
import org.jaguar.modules.numbering.mapper.RuleMapper;
import org.jaguar.modules.numbering.model.Rule;
import org.jaguar.modules.numbering.service.RuleService;
import org.springframework.http.ResponseEntity;
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
@Api(value = "编号规则表管理")
public class RuleController extends AbstractController<Rule, RuleMapper, RuleService> {

    @ApiOperation(value = "查询编号规则表")
    @RequiresPermissions("编号规则序列表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Rule>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<Rule> page,
            @ApiParam(value = "模糊编号规则名称") String fuzzyName) {

        LambdaQueryWrapper<Rule> wrapper = JaguarLambdaQueryWrapper.<Rule>newInstance()
                .eq(Rule::getName, fuzzyName);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "编号规则表详情")
    @RequiresPermissions("编号规则序列表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Rule>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "更新编号规则")
    @RequiresPermissions("编号规则序列表:新增编辑")
    @PostMapping
    public ResponseEntity<JsonResult<Rule>> update(@RequestBody @Valid Rule entity) {
        synchronized (this) {
            entity = service.createOrUpdate(entity);
        }
        return success(entity);
    }

    @ApiOperation(value = "删除编号规则表")
    @RequiresPermissions("numbering_rule_del")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<JsonResult<?>> del(@PathVariable Long id) {
        return super.delete(id);
    }

}