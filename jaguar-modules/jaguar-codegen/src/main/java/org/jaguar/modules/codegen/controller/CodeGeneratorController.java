package org.jaguar.modules.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.codegen.controller.dto.CodeGenerator;
import org.jaguar.modules.codegen.controller.vo.TableVO;
import org.jaguar.modules.codegen.mapper.CodeGeneratorMapper;
import org.jaguar.modules.codegen.service.CodeGeneratorService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@RestController
@Api(tags = "代码生成管理")
@RequestMapping("/codegen")
public class CodeGeneratorController extends BaseController<CodeGenerator, CodeGeneratorMapper, CodeGeneratorService> {

    @ApiOperation(value = "查询数据库表")
    @GetMapping(value = "/show_tables")
    public JsonResult<Page<TableVO>> showTables(@ApiIgnore Page<TableVO> page,
                                                @ApiParam(value = "数据源名称", required = true) @RequestParam @NotBlank String dataSourceName,
                                                @ApiParam(value = "模糊表名") @RequestParam(required = false) String fuzzyTableName) {

        if (page.orders().size() == 0) {
            page.orders().add(new OrderItem("table_name", false));
        }

        page = service.showTables(page, dataSourceName, fuzzyTableName);
        return success(page);
    }

    @ApiOperation(value = "生成代码")
    @PostMapping(value = "/generate")
    public void generate(@RequestBody @Valid CodeGenerator codeGenerator,
                         HttpServletResponse response) throws IOException {
        service.generate(codeGenerator, response);
    }

}
