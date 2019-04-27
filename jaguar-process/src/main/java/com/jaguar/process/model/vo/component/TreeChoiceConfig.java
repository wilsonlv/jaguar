package com.jaguar.process.model.vo.component;

/**
 * Created by lvws on 2019/4/25.
 */
public class TreeChoiceConfig extends ComponentConfig{

    /**
     * 标签的取值列
     */
    private String labelColumnKey;
    /**
     * 孩子的取值列
     */
    private String childrenColumnKey;
    /**
     * 叶子结点的取值列
     */
    private String leafColumnKey;
    /*
     * 叶子结点样式
     */
    private String leafClass;
    /**
     * 根节点样式
     */
    private String rootClass;

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

    public String getLeafColumnKey() {
        return leafColumnKey;
    }

    public void setLeafColumnKey(String leafColumnKey) {
        this.leafColumnKey = leafColumnKey;
    }

    public String getLeafClass() {
        return leafClass;
    }

    public void setLeafClass(String leafClass) {
        this.leafClass = leafClass;
    }

    public String getRootClass() {
        return rootClass;
    }

    public void setRootClass(String rootClass) {
        this.rootClass = rootClass;
    }
}
