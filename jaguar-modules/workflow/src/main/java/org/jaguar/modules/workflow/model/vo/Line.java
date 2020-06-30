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
public class Line implements Serializable {

    private String elementId;
    private String name;
    private String description;
    private String conditionExpression;

    private String startBox;
    private String endBox;
    private double startBoxPortX;
    private double startBoxPortY;
    private double endBoxPortX;
    private double endBoxPortY;

    private int isShowLabel;
    private int isShowEdge;

    private List<LineBend> lineBendList=new ArrayList<>();

}
