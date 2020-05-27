package org.jaguar.modules.numbering.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

/**
 * <p>
 * 编号规则序列表
 * </p>
 *
 * @author lvws
 * @since 2019-04-02
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_numbering_rule_serial")
public class RuleSerial extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 编号规则ID
     */
    @TableField("rule_id")
    private Long ruleId;
    /**
     * 编号规则条目ID
     */
    @TableField("rule_item_id")
    private Long ruleItemId;
    /**
     * 格式
     */
    @TableField("parttern_")
    private String parttern;
    /**
     * 序列号
     */
    @TableField("serial_number")
    private String serialNumber;

}