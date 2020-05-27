package org.jaguar.modules.numbering.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.numbering.mapper.RuleSerialMapper;
import org.jaguar.modules.numbering.model.RuleSerial;
import org.jaguar.modules.numbering.service.RuleSerialService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * <p>
 * 编号规则序列 前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Validated
@RestController
@RequestMapping("/numbering/rule_serial")
@Api(value = "编号规则序列管理")
public class RuleSerialController extends AbstractController<RuleSerial, RuleSerialMapper, RuleSerialService> {

    @ApiOperation(value = "查询编号规则序列")
    @RequiresPermissions("numbering_rule_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<RuleSerial>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<RuleSerial> page) {

        return super.query(page);
    }

    @ApiOperation(value = "编号规则序列详情")
    @RequiresPermissions("numbering_rule_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<RuleSerial>> detail(@PathVariable Long id) {
        return super.getById(id);
    }

}