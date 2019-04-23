package com.itqingning.jaguar.flowable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/3/5.
 */
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

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public String getStartBox() {
        return startBox;
    }

    public void setStartBox(String startBox) {
        this.startBox = startBox;
    }

    public String getEndBox() {
        return endBox;
    }

    public void setEndBox(String endBox) {
        this.endBox = endBox;
    }

    public double getStartBoxPortX() {
        return startBoxPortX;
    }

    public void setStartBoxPortX(double startBoxPortX) {
        this.startBoxPortX = startBoxPortX;
    }

    public double getStartBoxPortY() {
        return startBoxPortY;
    }

    public void setStartBoxPortY(double startBoxPortY) {
        this.startBoxPortY = startBoxPortY;
    }

    public double getEndBoxPortX() {
        return endBoxPortX;
    }

    public void setEndBoxPortX(double endBoxPortX) {
        this.endBoxPortX = endBoxPortX;
    }

    public double getEndBoxPortY() {
        return endBoxPortY;
    }

    public void setEndBoxPortY(double endBoxPortY) {
        this.endBoxPortY = endBoxPortY;
    }

    public int getIsShowLabel() {
        return isShowLabel;
    }

    public void setIsShowLabel(int isShowLabel) {
        this.isShowLabel = isShowLabel;
    }

    public int getIsShowEdge() {
        return isShowEdge;
    }

    public void setIsShowEdge(int isShowEdge) {
        this.isShowEdge = isShowEdge;
    }

    public List<LineBend> getLineBendList() {
        return lineBendList;
    }

    public void setLineBendList(List<LineBend> lineBendList) {
        this.lineBendList = lineBendList;
    }
}
