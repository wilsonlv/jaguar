package org.jaguar.modules.workflow.model.dto;

import lombok.Data;
import org.jaguar.modules.workflow.enums.ProcessTagType;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/6/24.
 */
@Data
public class ProcessTag implements Serializable {

    public static final String PROCESS_TAGS = "PROCESS_TAGS";
    public static final String TASK_TAGS = "TASK_TAGS";
    public static final String TASK_TAG_REJECT_NAME = "驳回";
    public static final String PROCESS_TAG_GUAQI_NAME = "挂起";

    private String label;
    private ProcessTagType type;
    private String content;

    public ProcessTag() {
    }

    public ProcessTag(String label, ProcessTagType type) {
        this.label = label;
        this.type = type;
    }

    public ProcessTag(String label, ProcessTagType type, String content) {
        this.label = label;
        this.type = type;
        this.content = content;
    }
}
