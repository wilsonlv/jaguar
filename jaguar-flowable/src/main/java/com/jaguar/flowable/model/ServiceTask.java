package com.jaguar.flowable.model;

/**
 * Created by lvws on 2019/3/1.
 */
public class ServiceTask extends Node {

    private String implementation;
    private String implementationType;

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(String implementationType) {
        this.implementationType = implementationType;
    }
}
