package org.jaguar.modules.workflow.model.vo.component;

import com.alibaba.fastjson.JSONObject;
import org.jaguar.core.context.SpringContext;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.workflow.interfaces.IUserDefinedComponent;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/19.
 */
public abstract class ComponentConfig implements Serializable {

    /**
     * 通过解析自定义组件配置来获取自定义组件的实现类
     */
    public static IUserDefinedComponent resovleUserDefinedComponent(String userDefinedConfig) {
        UserDefinedConfig config = JSONObject.parseObject(userDefinedConfig, UserDefinedConfig.class);
        Object bean = SpringContext.getBean(config.getComponentClassName());
        if (!(bean instanceof IUserDefinedComponent)) {
            throw new CheckedException("自定义组件类必须是实现IUserDefinedComponent接口！");
        }
        return (IUserDefinedComponent) bean;
    }

}
