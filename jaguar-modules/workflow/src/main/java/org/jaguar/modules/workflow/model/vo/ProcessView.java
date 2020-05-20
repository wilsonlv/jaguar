package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lvws
 * @since 2019/3/14.
 */
@Data
public class ProcessView implements Serializable {

    private FlowDefinition flowDefinition;
    private List<ActivityElement> activityElementList = new ArrayList<>();

}
