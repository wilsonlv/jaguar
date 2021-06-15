package org.jaguar.modules.numbering.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.modules.numbering.enums.NumberingRuleItemType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 编号规则条目
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_numbering_rule_item")
public class RuleItem extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 编号规则ID
     */
    @ApiModelProperty(value = "编号规则ID", required = true)
    @NotNull(message = "编号规则ID为非空")
    @TableField("rule_id")
    private Long ruleId;
    /**
     * 条目类型（固定类型：FIXED，日期类型：DATETIME，流水号：SERIAL_NUMBER，sql查询：SQL_QUERY）
     */
    @ApiModelProperty(value = "条目类型", required = true)
    @NotNull(message = "条目类型为非空")
    @TableField("numbering_rule_item_type")
    private NumberingRuleItemType numberingRuleItemType;
    /**
     * 流水序列等级
     */
    @ApiModelProperty(value = "流水序列等级")
    @TableField("level_")
    private Integer level;
    /**
     * 条目名称（固定类型：固定的字符、日期类型：日期格式，流水号：初试值，sql查询：查询的sql语句）
     */
    @ApiModelProperty(value = "条目名称", required = true)
    @NotBlank(message = "条目名称为非空")
    @TableField("name_")
    private String name;
    /**
     * 是否在编号中显示
     */
    @ApiModelProperty(value = "是否在编号中显示", required = true)
    @NotNull(message = "是否在编号中显示为非空")
    @TableField("show_")
    private Boolean show;
    /**
     * 是否影响流水号
     */
    @ApiModelProperty(value = "是否影响流水号", required = true)
    @NotNull(message = "是否影响流水号为非空")
    @TableField("effect_")
    private Boolean effect;
    /**
     * 条目长度
     */
    @ApiModelProperty(value = "条目长度")
    @TableField("length_")
    private Integer length;
    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号", required = true)
    @NotNull(message = "排序号为非空")
    @TableField("sort_no")
    private Integer sortNo;

}