package com.itqingning.jaguar.flowable.model;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/19.
 */
public class FieldColumn implements Serializable {

    private String label;
    private String key;
    private boolean visiable;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FieldColumn{");
        sb.append("label='").append(label).append('\'');
        sb.append(", key='").append(key).append('\'');
        sb.append(", visiable=").append(visiable);
        sb.append('}');
        return sb.toString();
    }
}
