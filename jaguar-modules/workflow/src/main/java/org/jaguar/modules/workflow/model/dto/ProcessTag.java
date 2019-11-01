package org.jaguar.modules.workflow.model.dto;

import lombok.Data;
import org.jaguar.modules.workflow.enums.ProcessTagType;

import java.io.Serializable;

/**
 * Created by lvws on 2019/6/24.
 */
@Data
public class ProcessTag implements Serializable {

    private String label;
    private ProcessTagType type;

    public ProcessTag() {
    }

    public ProcessTag(String label, ProcessTagType type) {
        this.label = label;
        this.type = type;
    }
}
