package org.jaguar.modules.code.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ArrayUtils;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.code.generator.mapper.CodeGeneratorMapper;
import org.jaguar.modules.code.generator.model.CodeGenerator;
import org.jaguar.modules.code.generator.service.CodeGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@RestController
@Api(value = "代码生成管理", description = "代码生成管理")
@RequestMapping("/code_generator")
public class CodeGeneratorController extends AbstractController<CodeGenerator, CodeGeneratorMapper, CodeGeneratorService> {

    @ApiOperation(value = "查询系统配置表")
    @GetMapping(value = "/show_tables")
    public ResponseEntity<JsonResult<Page<CodeGenerator>>> showTables(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<CodeGenerator> page,
            @ApiParam(value = "模糊表名") @RequestParam(required = false) String fuzzyTableName) {

        if (ArrayUtils.isEmpty(page.ascs()) && ArrayUtils.isEmpty(page.descs())) {
            page.setDesc("table_name");
        }

        page = service.showTables(page, fuzzyTableName);
        return success(page);
    }

    @ApiOperation(value = "生成代码")
    @PostMapping(value = "/generate")
    public ResponseEntity<JsonResult> generate(@ApiParam(value = "表名", required = true) @RequestParam String tableName,
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
