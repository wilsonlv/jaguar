package top.wilsonlv.jaguar.cloud.handlerlog.client.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Data
public class HandlerLogSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String createBy;
    /**
     * 创建人ID
     */
    private Long createUserId;

}
