package org.jaguar.support.dubbo.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jaguar.core.base.BaseModel;
import org.jaguar.core.web.LoginUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2019/5/23.
 */
public class Parameter implements Serializable {

    private String service;
    private String method;

    private Long id;
    private BaseModel model;
    private Map<?, ?> map;
    private Page<?> page;
    private List<?> list;
    private Object[] objects;
    private Boolean flag;
    private String str;
    private Object object;

    private Long currentUserId;

    public static final String GET_BY_ID = "getById";
    public static final String INSERT = "insert";
    public static final String UPDATE_BY_ID = "updateById";
    public static final String SAVE_OR_UPDATE = "saveOrUpdate";
    public static final String DELETE = "delete";

    public static Parameter createInstance(String service) {
        return createInstance(service, null);
    }

    public static Parameter createInstance(String service, String method) {
        return new Parameter(LoginUtil.getCurrentUser(), service, method);
    }

    public static Parameter createInstance(Long currentUserId, String service, String method) {
        return new Parameter(currentUserId, service, method);
    }

    protected Parameter(Long currentUserId, String service, String method) {
        this.currentUserId = currentUserId;
        this.service = service;
        this.method = method;
    }

    protected Parameter(Object result) {
        if (result instanceof Long) {
            this.id = (Long) result;
        } else if (result instanceof BaseModel) {
            this.model = (BaseModel) result;
        } else if (result instanceof Page) {
            this.page = (Page<?>) result;
        } else if (result instanceof Map<?, ?>) {
            this.map = (Map<?, ?>) result;
        } else if (result instanceof List<?>) {
            this.list = (List<?>) result;
        } else if (result instanceof Boolean) {
            this.flag = (Boolean) result;
        } else if (result instanceof String) {
            this.str = (String) result;
        } else {
            this.object = result;
        }
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public Parameter setMethod(String method) {
        this.method = method;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Parameter setId(Long id) {
        this.id = id;
        return this;
    }

    public BaseModel getModel() {
        return model;
    }

    public Parameter setModel(BaseModel model) {
        this.model = model;
        return this;
    }

    public Map<?, ?> getMap() {
        return map;
    }

    public Parameter setMap(Map<?, ?> map) {
        this.map = map;
        return this;
    }

    public Page<?> getPage() {
        return page;
    }

    public Parameter setPage(Page<?> page) {
        this.page = page;
        return this;
    }

    public List<?> getList() {
        return list;
    }

    public Parameter setList(List<?> list) {
        this.list = list;
        return this;
    }

    public Object[] getObjects() {
        return objects;
    }

    public Parameter setObjects(Object[] objects) {
        this.objects = objects;
        return this;
    }

    public Boolean getFlag() {
        return flag;
    }

    public Parameter setFlag(Boolean flag) {
        this.flag = flag;
        return this;
    }

    public String getStr() {
        return str;
    }

    public Parameter setStr(String str) {
        this.str = str;
        return this;
    }

    public Object getObject() {
        return object;
    }

    public Parameter setObject(Object object) {
        this.object = object;
        return this;
    }

    protected Long getCurrentUserId() {
        return this.currentUserId;
    }

    public Parameter getById(Long id) {
        this.id = id;
        this.method = GET_BY_ID;
        return this;
    }

    public Parameter insert(BaseModel model) {
        this.model = model;
        this.method = INSERT;
        return this;
    }

    public Parameter updateById(BaseModel model) {
        this.model = model;
        this.method = UPDATE_BY_ID;
        return this;
    }

    public Parameter saveOrUpdate(BaseModel model) {
        this.model = model;
        this.method = SAVE_OR_UPDATE;
        return this;
    }

    public Parameter delete(Long id) {
        this.id = id;
        this.method = DELETE;
        return this;
    }

}