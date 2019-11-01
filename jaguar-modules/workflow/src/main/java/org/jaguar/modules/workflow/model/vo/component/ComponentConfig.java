package org.jaguar.modules.workflow.model.vo.component;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by lvws on 2019/3/19.
 */
public abstract class ComponentConfig implements Serializable {

    /**
     * 通过解析自定义组件配置来获取自定义组件的实现类
     */
    public static <T extends ComponentConfig> T resovleComponent(String userDefinedConfig, Class<T> clazz) {
        return JSONObject.parseObject(userDefinedConfig, clazz);
    }

}
