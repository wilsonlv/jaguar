package top.wilsonlv.jaguar.codegen.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2021/6/18
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class PreviewDTO extends CodegenDTO {

    @NotBlank(message = "模板文件为非空")
    @ApiModelProperty(value = "模板文件", required = true)
    private String codeTemplateFile;

}
