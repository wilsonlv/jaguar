package org.jaguar.modules.codegen.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.modules.codegen.enums.CodeTemplateType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lvws
 * @since 2021/6/18
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class PreviewDTO extends CodegenDTO {

    @NotNull(message = "模板类型为非空")
    @ApiModelProperty(value = "模板类型", required = true)
    private CodeTemplateType codeTemplateType;

    @NotBlank(message = "模板文件为非空")
    @ApiModelProperty(value = "模板文件", required = true)
    private String codeTemplateFile;

}
