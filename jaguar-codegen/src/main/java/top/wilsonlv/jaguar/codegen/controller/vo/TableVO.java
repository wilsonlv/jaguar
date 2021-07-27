package top.wilsonlv.jaguar.codegen.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lvws
 * @since 2021/6/17
 */
@Data
@ApiModel
public class TableVO {

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表注释")
    private String tableComment;

    @ApiModelProperty(value = "引擎")
    private String engine;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
