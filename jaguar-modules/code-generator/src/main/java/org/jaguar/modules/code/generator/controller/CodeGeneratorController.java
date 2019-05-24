package org.jaguar.modules.code.generator.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.modules.code.generator.mapper.CodeGeneratorMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;
import org.jaguar.modules.code.generator.service.CodeGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lvws on 2019/4/30.
 */
@RestController
@Api(value = "代码生成管理", description = "代码生成管理")
@RequestMapping("/table_info")
public class CodeGeneratorController extends AbstractController<CodeGenerator, CodeGeneratorMapper, CodeGeneratorService> {

    @ApiOperation(value = "查询系统配置表")
    @RequiresPermissions("tableInfo:read")
    @GetMapping(value = "/show_tables")
    public ResponseEntity<JsonResult<org.jaguar.core.web.Page<CodeGenerator>>> showTables(
            @ApiParam(value = "分页信息") Page<CodeGenerator> page,
            @ApiParam(value = "模糊表名") String fuzzyTableName) {

        if (ArrayUtils.isEmpty(page.ascs()) && ArrayUtils.isEmpty(page.descs())) {
            page.setDesc("table_name");
        }

        Page<CodeGenerator> tableInfos = service.showTables(page, fuzzyTableName);
        return success(tableInfos);
    }

    @ApiOperation(value = "生成代码")
    @RequiresPermissions("tableInfo:update")
    @PostMapping(value = "/generate")
    public ResponseEntity<JsonResult> generate(
            @ApiParam(value = "表名", required = true) @RequestParam String tableName,
            @ApiParam(value = "包名", required = true) @RequestParam String parentPackage,
            @ApiParam(value = "作者", required = true) @RequestParam String author,
            @ApiParam(value = "文件输出目录", required = true) @RequestParam String outputDir) {

        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.setTableName(tableName);
        codeGenerator.setParentPackage(parentPackage);
        codeGenerator.setAuthor(author);
        codeGenerator.setOutputDir(outputDir);

        service.generate(codeGenerator);
        return success();
    }

}
