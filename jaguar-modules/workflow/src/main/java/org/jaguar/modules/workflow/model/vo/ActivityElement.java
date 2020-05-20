package org.jaguar.modules.workflow.model.vo;

import lombok.Data;
import org.jaguar.modules.workflow.enums.TaskStatus;
import org.jaguar.modules.workflow.model.po.IProcessUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lvws
 * @since 2019/3/14.
 */
@Data
public class ActivityElement implements Serializable {

    private String id;
    private String name;
    private String type;
    private IProcessUser assignee;
    private String startTime;
    private String endTime;
    private TaskStatus taskStatus;

    private List<IProcessUser> candidateUsers = new ArrayList<>();
    private List<String> candidateGroups = new ArrayList<>();

}
