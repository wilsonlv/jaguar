package top.wilsonlv.jaguar.codegen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.wilsonlv.jaguar.codegen.mapper.CodeTemplateMapper;
import top.wilsonlv.jaguar.codegen.entity.CodeTemplate;
import top.wilsonlv.jaguar.codegen.service.CodeTemplateService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        return success(CodeTemplateService.CODE_TEMPLATE_DATA_BASE.values()
                .stream().sorted(Comparator.comparing(CodeTemplate::getCodeTemplateName)).collect(Collectors.toList()));
    }

    @ApiOperation(value = "编辑代码模板")
    @PostMapping
    public JsonResult<Void> modify(@RequestBody @Valid CodeTemplate codeTemplate) {
        synchronized (this) {
            service.modify(codeTemplate);
        }
        return success();
    }

}