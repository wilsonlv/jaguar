package org.jaguar.modules.workflow.model.vo.component;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.core.context.SpringContext;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.workflow.enums.FormDataPersistenceType;
import org.jaguar.modules.workflow.interfaces.IUserDefinedComponent;

/**
 * Created by lvws on 2019/4/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDefinedConfig extends ComponentConfig {

    private String componentJsName;
    /**
     * spring bean的name
     * 需要实现com.jaguar.flowable.interfaces.IUserDefinedComponent
     */
    private String componentClassName;

    private FormDataPersistenceType formDataPersistenceType;

    public IUserDefinedComponent getBean() {
        if (StringUtils.isBlank(componentClassName)) {
            return null;
        }

        Object bean = SpringContext.getBean(componentClassName);
        if (!(bean instanceof IUserDefinedComponent)) {
            throw new CheckedException("自定义组件类必须是实现IUserDefinedComponent接口！");
        }
        return (IUserDefinedComponent) bean;
    }

}