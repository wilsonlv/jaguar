package org.jaguar.modules.workflow.model.vo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

}