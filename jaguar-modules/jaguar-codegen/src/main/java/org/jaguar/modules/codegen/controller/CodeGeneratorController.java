package org.jaguar.modules.codegen.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.codegen.config.CodeGeneratorProperties;
import org.jaguar.modules.codegen.mapper.CodeGeneratorMapper;
import org.jaguar.modules.codegen.controller.dto.CodeGenerator;
import org.jaguar.modules.codegen.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@RestController
@Api(tags = "代码生成管理")
@RequestMapping("/code_generator")
public class CodeGeneratorController extends BaseController<CodeGenerator, CodeGeneratorMapper, CodeGeneratorService> {

    @Autowired
    private CodeGeneratorProperties codeGeneratorProperties;

    @ApiOperation(value = "查询数据库表")
    @GetMapping(value = "/show_tables")
    public JsonResult<Page<CodeGenerator>> showTables(
            @ApiIgnore Page<CodeGenerator> page,
            @ApiParam(value = "模糊表名") @RequestParam(required = false) String fuzzyTableName) {

        if (page.orders().size() == 0) {
            page.orders().add(new OrderItem("table_name", false));
        }

        page = service.showTables(page, fuzzyTableName);
        return success(page);
    }

    @ApiOperation(value = "查询代码生成配置")
    @GetMapping(value = "/config")
    public JsonResult<Object> config() {
        JSONObject config = new JSONObject();
        config.put("defaultOutputDir", codeGeneratorProperties.getDefaultOutputDir());
        config.put("defaultParentPackage", codeGeneratorProperties.getDefaultParentPackage());
        config.put("defaultTablePrefix", codeGeneratorProperties.getDefaultTablePrefix());
        return success(config);
    }

    @ApiOperation(value = "生成代码")
    @PostMapping(value = "/generate")
    public JsonResult<?> generate(@RequestBody @Valid CodeGenerator codeGenerator) {

        if (StringUtils.isBlank(codeGenerator.getOutputDir())) {
            codeGenerator.setOutputDir(codeGeneratorProperties.getDefaultOutputDir());
        }
        if (StringUtils.isBlank(codeGenerator.getParentPackage())) {
            codeGenerator.setParentPackage(codeGeneratorProperties.getDefaultParentPackage());
        }
        if (StringUtils.isBlank(codeGenerator.getTablePrefix())) {
            codeGenerator.setTablePrefix(codeGeneratorProperties.getDefaultTablePrefix());
        }

        service.generate(codeGenerator);
        return success();
    }

}
