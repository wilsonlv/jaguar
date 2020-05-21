package org.jaguar.modules.workflow.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

import java.time.LocalDateTime;

/**
 * 工单备注表
 *
 * @author lvws
 * @date 2020-04-25 17:06:48
 */
@Data
@TableName("jaguar_modules_workflow_process_remark")
@EqualsAndHashCode(callSuper = true)
public class ProcessRemark extends BaseModel {

    /**
     * 工单ID
     */
    @TableField("process_info_id")
    private Long processInfoId;
    /**
     * 记录人
     */
    @TableField("remark_user")
    private String remarkUser;
    /**
     * 记录时间
     */
    @TableField("remark_time")
    private LocalDateTime remarkTime;
    /**
     * 记录内容
     */
    @TableField("remark_content")
    private String remarkContent;

}
