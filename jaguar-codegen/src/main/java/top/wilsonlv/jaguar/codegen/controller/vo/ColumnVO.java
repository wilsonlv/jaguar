package top.wilsonlv.jaguar.codegen.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/6/19
 */
@Data
@ApiModel
public class ColumnVO implements Serializable {

    @ApiModelProperty("列名")
    private String columnName;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty("列类型")
    private String columnType;

    @ApiModelProperty("列注释")
    private String columnComment;

    @ApiModelProperty("字段名")
    private String fieldName;

    @ApiModelProperty("字段方法名")
    private String fieldMethodName;

    @ApiModelProperty("字段类型")
    private String filedType;

}
