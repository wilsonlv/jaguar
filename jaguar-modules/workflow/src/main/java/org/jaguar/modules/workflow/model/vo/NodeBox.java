package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/5.
 */
@Data
public class NodeBox implements Serializable {

    private String name;
    private String type;
    private double x;
    private double y;
    private double width;
    private double height;

}
