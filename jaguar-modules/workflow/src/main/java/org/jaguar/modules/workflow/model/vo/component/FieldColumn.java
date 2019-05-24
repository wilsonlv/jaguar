package org.jaguar.modules.workflow.model.vo.component;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/19.
 */
@Data
public class FieldColumn implements Serializable {

    private String label;
    private String key;
    private boolean visible;

}
