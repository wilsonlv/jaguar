package com.itqingning.jaguar.flowable.model;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/2.
 */
public class FlowableEvent implements Serializable {

    private String type;
    private String ImplementationType;
    private String Implementation;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImplementationType() {
        return ImplementationType;
    }

    public void setImplementationType(String implementationType) {
        ImplementationType = implementationType;
    }

    public String getImplementation() {
        return Implementation;
    }

    public void setImplementation(String implementation) {
        Implementation = implementation;
    }
}
