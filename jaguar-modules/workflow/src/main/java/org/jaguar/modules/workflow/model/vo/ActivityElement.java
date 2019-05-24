package org.jaguar.modules.workflow.model.vo;

import org.jaguar.modules.workflow.enums.TaskStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/14.
 */
@Data
public class ActivityElement implements Serializable {

    private String id;
    private String name;
    private String type;
    private String assignee;
    private String startTime;
    private String endTime;
    private TaskStatus taskStatus;

}
