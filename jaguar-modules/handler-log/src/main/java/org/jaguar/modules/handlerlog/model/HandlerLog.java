package org.jaguar.modules.handlerlog.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;


import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
@Data
@TableName("jaguar_modules_handler_log")
@EqualsAndHashCode(callSuper = true)
public class HandlerLog extends BaseModel {

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
     * 错误信息
     */
    @TableField("error_msg")
    private String errorMsg;
    /**
     * 响应时长
     */
    @TableField("duration_")
    private Long duration;

}
