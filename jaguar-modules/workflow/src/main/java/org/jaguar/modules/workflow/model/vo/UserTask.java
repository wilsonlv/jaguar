package org.jaguar.modules.workflow.model.vo;

import org.jaguar.modules.workflow.Constant;
import org.jaguar.modules.workflow.enums.TaskFieldPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvws on 2019/3/1.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserTask extends Node {

    /**
     * 单一执行人
     */
    private String assignee;
    /**
     * 候选组
     */
    private String candidateGroup;
    /**
     * 多执行人
     */
    private String multiInstanceAssignee;
    /**
     * 多执行人，任务完成条件
     */
    private String completionCondition = Constant.DEFAULT_COMPLETION_CONDITION;
    /**
     * 多执行人，顺序执行
     */
    private Boolean sequential = false;

    /**
     * 当前任务对应的表单块keys
     */
    private List<String> formKeys;
    /**
     * 当前任务对应的表单表单权限
     */
    private Map<String, TaskFieldPermission> taskFormPermissionMap = new HashMap<>();
    /**
     * 任务事件
     */
    private List<FlowableEvent> flowableEvents = new ArrayList<>();

}
