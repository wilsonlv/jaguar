package org.jaguar.modules.codegen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.mapper.CodeTemplateMapper;
import org.jaguar.modules.codegen.model.CodeTemplate;
import org.jaguar.modules.codegen.service.CodeTemplateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * @author lvws
 * @since 2021-06-17
 */
@Validated
@RestController
@RequestMapping("/code_template")
@Api(tags = "代码模板管理")
public class CodeTemplateController extends BaseController<CodeTemplate, CodeTemplateMapper, CodeTemplateService> {

    @ApiOperation(value = "查询代码模板管")
    @GetMapping(value = "/list")
    public JsonResult<Collection<CodeTemplate>> list() {
        return success(CodeTemplateService.CODE_TEMPLATE_DATA_BASE.values());
    }

    @ApiOperation(value = "编辑代码模板")
    @PostMapping("/{codeTemplateType}")
    public JsonResult<Void> modify(@PathVariable CodeTemplateType codeTemplateType,
                                   @ApiParam(value = "编辑代码文件", required = true) @RequestBody @NotBlank String codeTemplateFile) {
        synchronized (this) {
            service.modify(codeTemplateType, codeTemplateFile);
        }
        return success();
    }

}