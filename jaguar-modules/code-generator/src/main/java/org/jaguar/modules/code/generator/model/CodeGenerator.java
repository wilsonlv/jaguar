package org.jaguar.modules.code.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
    @NotBlank
    private String moduleName;
    @ApiModelProperty(value = "表名前缀")
    private String tablePrefix;
    @ApiModelProperty(value = "包名")
    private String parentPackage;
    @ApiModelProperty(value = "输出路径")
    private String outputDir;

    @ApiModelProperty(value = "备注")
    private String tableComment;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
