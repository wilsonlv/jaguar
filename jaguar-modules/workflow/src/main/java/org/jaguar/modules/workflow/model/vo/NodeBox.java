package org.jaguar.modules.workflow.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/3/14.
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
