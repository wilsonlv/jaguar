package com.jaguar.process.model.vo.component;

/**
 * Created by lvws on 2019/4/26.
 */
public class UserDefinedConfig extends ComponentConfig {

    private String componentJsName;
    /**
     * spring bean的name
     * 需要实现com.jaguar.flowable.interfaces.IUserDefinedComponent
     */
    private String componentClassName;

    public String getComponentJsName() {
        return componentJsName;
    }

    public void setComponentJsName(String componentJsName) {
        this.componentJsName = componentJsName;
    }

    public String getComponentClassName() {
        return componentClassName;
    }

    public void setComponentClassName(String componentClassName) {
        this.componentClassName = componentClassName;
    }

}
