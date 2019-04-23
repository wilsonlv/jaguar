package com.jaguar.sys.log.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;


import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
@TableName("sys_log")
public class SysLog extends BaseModel {

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;
    /**
     * 访问时间
     */
    @TableField("access_time")
    private Date accessTime;
    /**
     * 客户端IP
     */
    @TableField("client_host")
    private String clientHost;
    /**
     * 请求地址
     */
    @TableField("request_uri")
    private String requestUri;
    /**
     * 接口操作名称
     */
    @TableField("api_operation")
    private String apiOperation;
    /**
     * 请求参数
     */
    @TableField("parameters_")
    private String parameters;
    /**
     * 请求方式
     */
    @TableField("method_")
    private String method;
    /**
     * 客户端引擎
     */
    @TableField("user_agent")
    private String userAgent;
    /**
     * http响应状态码
     */
    @TableField("status_")
    private Integer status;
    /**
     * 响应时长
     */
    @TableField("duration_")
    private Long duration;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getApiOperation() {
        return apiOperation;
    }

    public void setApiOperation(String apiOperation) {
        this.apiOperation = apiOperation;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
