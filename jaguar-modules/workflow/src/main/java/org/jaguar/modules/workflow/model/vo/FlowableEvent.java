package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/2.
 */
@Data
public class FlowableEvent implements Serializable {

    private String type;
    private String ImplementationType;
    private String Implementation;

}
