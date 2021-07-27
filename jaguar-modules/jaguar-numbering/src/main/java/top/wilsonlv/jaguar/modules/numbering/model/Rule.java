package top.wilsonlv.jaguar.modules.numbering.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 编号规则表
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_numbering_rule")
public class Rule extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称）", required = true)
    @NotBlank(message = "规则名称为非空")
    @TableField("name_")
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField("description_")
    private String description;

}