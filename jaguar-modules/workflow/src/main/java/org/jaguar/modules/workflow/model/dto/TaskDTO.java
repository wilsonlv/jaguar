package org.jaguar.modules.workflow.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lvws on 2019/3/27.
 */
@Data
public class TaskDTO implements Serializable {

    private String taskDefId;
    private String taskId;
    private String taskName;
    private String assignee;
    private Date startTime;
    private Date endTime;

}
