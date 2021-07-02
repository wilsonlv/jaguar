package org.jaguar.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jaguar.codegen.controller.dto.CodegenDTO;
import org.jaguar.codegen.service.CodeGeneratorService;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.codegen.controller.dto.PreviewDTO;
import org.jaguar.codegen.controller.vo.TableVO;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * @author lvws
 * @since 2021-06-16
 */
@RestController
@Api(tags = "代码生成管理")
@RequestMapping("/codegen")
@RequiredArgsConstructor
public class CodegenController {

    private final CodeGeneratorService codeGeneratorService;

    @ApiOperation(value = "查询数据库表")
    @GetMapping(value = "/show_tables")
    public JsonResult<Page<TableVO>> showTables(@ApiIgnore Page<TableVO> page,
                                                @ApiParam(value = "数据源名称", required = true) @RequestParam @NotBlank String dataSourceName,
                                                @ApiParam(value = "模糊表名") @RequestParam(required = false) String fuzzyTableName) {

        if (page.orders().size() == 0) {
            page.orders().add(new OrderItem("table_name", false));
        }

        page = codeGeneratorService.showTables(page, dataSourceName, fuzzyTableName);
        return JsonResult.success(page);
    }

    @ApiOperation(value = "生成代码")
    @PostMapping(value = "/generate")
    public void generate(@RequestBody @Valid CodegenDTO codegen,
                         HttpServletResponse response) throws IOException {
        codeGeneratorService.generate(codegen, response);
    }

    @ApiOperation(value = "预览")
    @PostMapping(value = "/preview")
    public JsonResult<String> preview(@RequestBody @Valid PreviewDTO preview) throws IOException {
        return JsonResult.success(codeGeneratorService.preview(preview));
    }

    @ApiOperation(value = "预览真实文件名")
    @GetMapping(value = "/preview/fileName")
    public JsonResult<String> previewFileName(@RequestParam @NotBlank String tableName,
                                              @RequestParam(required = false) String tablePrefix,
                                              @RequestParam @NotBlank String fileName) {
        return JsonResult.success(codeGeneratorService.previewFileName(tableName, tablePrefix, fileName));
    }

    @ApiOperation(value = "预览真实文件路径")
    @GetMapping(value = "/preview/filePath")
    public JsonResult<String> previewFilePath(@RequestParam @NotBlank String parentPackage,
                                              @RequestParam @NotBlank String moduleName,
                                              @RequestParam @NotBlank String filePath) {
        return JsonResult.success(codeGeneratorService.previewFilePath(parentPackage, moduleName, filePath));
    }

}
