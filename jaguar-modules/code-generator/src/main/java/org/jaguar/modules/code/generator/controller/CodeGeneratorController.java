package org.jaguar.modules.code.generator.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.code.generator.config.CodeGeneratorProperties;
import org.jaguar.modules.code.generator.mapper.CodeGeneratorMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;
import org.jaguar.modules.code.generator.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@RestController
@Api(value = "代码生成管理")
@RequestMapping("/code_generator")
public class CodeGeneratorController extends AbstractController<CodeGenerator, CodeGeneratorMapper, CodeGeneratorService> {

    @Autowired
    private CodeGeneratorProperties codeGeneratorProperties;

    @ApiOperation(value = "查询数据库表")
    @GetMapping(value = "/show_tables")
    public ResponseEntity<JsonResult<Page<CodeGenerator>>> showTables(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<CodeGenerator> page,
            @ApiParam(value = "模糊表名") @RequestParam(required = false) String fuzzyTableName) {

        if (ArrayUtils.isEmpty(page.ascs()) && ArrayUtils.isEmpty(page.descs())) {
            page.setDesc("table_name");
        }

        page = service.showTables(page, fuzzyTableName);
        return success(page);
    }

    @ApiOperation(value = "查询代码生成配置")
    @GetMapping(value = "/config")
    public ResponseEntity<JsonResult<Object>> config() {
        JSONObject config = new JSONObject();
        config.put("defaultOutputDir", codeGeneratorProperties.getDefaultOutputDir());
        config.put("defaultParentPackage", codeGeneratorProperties.getDefaultParentPackage());
        return success(config);
    }

    @ApiOperation(value = "生成代码")
    @PostMapping(value = "/generate")
    public ResponseEntity<JsonResult<?>> generate(@RequestBody @Valid CodeGenerator codeGenerator) {

        if (StringUtils.isBlank(codeGenerator.getOutputDir())) {
            codeGenerator.setOutputDir(codeGeneratorProperties.getDefaultOutputDir());
        }
        if (StringUtils.isBlank(codeGenerator.getParentPackage())) {
            codeGenerator.setParentPackage(codeGeneratorProperties.getDefaultParentPackage());
        }

        service.generate(codeGenerator);
        return success();
    }

}
