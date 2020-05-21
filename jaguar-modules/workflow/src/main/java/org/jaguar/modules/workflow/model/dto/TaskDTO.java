package org.jaguar.modules.workflow.model.dto;

import lombok.Data;
import org.jaguar.modules.workflow.model.po.IProcessUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvws
 * @since 2019/3/27.
 */
@Data
public class TaskDTO implements Serializable {

    private String taskDefId;
    private String taskId;
    private String taskName;
    private IProcessUser assignee;
    private List<String> candidateGroup = new ArrayList<>();
    private List<IProcessUser> candidateUser = new ArrayList<>();
    private Date startTime;
    private Date endTime;
    private String description;

    private List<ProcessTag> tags = new ArrayList<>();

}
