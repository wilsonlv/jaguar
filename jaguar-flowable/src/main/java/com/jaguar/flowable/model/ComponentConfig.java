package com.jaguar.flowable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/3/19.
 */
public class ComponentConfig implements Serializable {

    /**
     * 数据源接口
     */
    private String dataSource;
    /**
     * 展示列
     */
    private List<FieldColumn> showColumns = new ArrayList<>();
    /**
     * 字段值的列key
     */
    private String valueColumnKey;
    /**
     * 查询参数
     */
    private List<FieldColumn> searchParams = new ArrayList<>();
    /**
     * 是否多选
     */
    private boolean multipart;
    /**
     * 树状结构标签的取值列
     */
    private String labelColumnKey;
    /**
     * 树状结构孩子的取值列
     */
    private String childrenColumnKey;


    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<FieldColumn> getShowColumns() {
        return showColumns;
    }

    public void setShowColumns(List<FieldColumn> showColumns) {
        this.showColumns = showColumns;
    }

    public String getValueColumnKey() {
        return valueColumnKey;
    }

    public void setValueColumnKey(String valueColumnKey) {
        this.valueColumnKey = valueColumnKey;
    }

    public List<FieldColumn> getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(List<FieldColumn> searchParams) {
        this.searchParams = searchParams;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    public String getLabelColumnKey() {
        return labelColumnKey;
    }

    public void setLabelColumnKey(String labelColumnKey) {
        this.labelColumnKey = labelColumnKey;
    }

    public String getChildrenColumnKey() {
        return childrenColumnKey;
    }

    public void setChildrenColumnKey(String childrenColumnKey) {
        this.childrenColumnKey = childrenColumnKey;
    }
}
