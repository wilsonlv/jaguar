package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/3/14.
 */
@Data
public class FlowableEvent implements Serializable {

    private String type;
    private String implementationType;
    private String implementation;

}
