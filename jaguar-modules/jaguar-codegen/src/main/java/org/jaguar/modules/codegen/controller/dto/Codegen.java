package org.jaguar.modules.codegen.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Data
@ApiModel
public class Codegen implements Serializable {

    @NotBlank(message = "数据源为非空")
    @ApiModelProperty(value = "数据源", required = true)
    private String dataSourceName;

    @NotBlank(message = "表名为非空")
    @ApiModelProperty(value = "表名", required = true)
    private String tableName;

    @ApiModelProperty(value = "表名前缀")
    private String tablePrefix;

    @NotBlank(message = "包名为非空")
    @ApiModelProperty(value = "包名", required = true)
    private String parentPackage;

    @NotBlank(message = "模块名为非空")
    @ApiModelProperty(value = "模块名", required = true)
    private String moduleName;

    @NotBlank(message = "作者为非空")
    @ApiModelProperty(value = "作者", required = true)
    private String author;

}
