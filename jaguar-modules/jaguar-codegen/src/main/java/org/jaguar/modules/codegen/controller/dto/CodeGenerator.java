package org.jaguar.modules.codegen.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class CodeGenerator extends BaseModel {

    @ApiModelProperty(value = "表名", required = true)
    @NotBlank(message = "表名为非空")
    private String tableName;
    @ApiModelProperty(value = "作者", required = true)
    @NotBlank(message = "作者为非空")
    private String author;

    @ApiModelProperty(value = "模块名", required = true)
    @NotBlank(message = "模块名为非空")
    private String moduleName;
    @ApiModelProperty(value = "表名前缀")
    private String tablePrefix;
    @ApiModelProperty(value = "包名")
    private String parentPackage;
    @ApiModelProperty(value = "输出路径")
    private String outputDir;

    @ApiModelProperty(value = "备注")
    private String tableComment;

}
