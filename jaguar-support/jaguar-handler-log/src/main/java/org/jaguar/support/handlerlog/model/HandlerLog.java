package org.jaguar.support.handlerlog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2018/11/9.
 */
@Data
@Document(indexName = "handler_log")
public class HandlerLog implements Serializable {

    /**
     * ID
     */
    @Id
    private Long id;

    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * accessToken
     */
    private String accessToken;
    /**
     * 访问时间
     */
    private LocalDateTime accessTime;
    /**
     * 客户端IP
     */
    private String clientHost;
    /**
     * 请求地址
     */
    private String requestUri;
    /**
     * 接口操作名称
     */
    private String apiOperation;
    /**
     * 请求参数
     */
    private String parameters;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 客户端引擎
     */
    private String userAgent;
    /**
     * http响应状态码
     */
    private Integer status;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 响应时长
     */
    private Long duration;
    /**
     * 创建人
     */
    private Long createBy;

}
