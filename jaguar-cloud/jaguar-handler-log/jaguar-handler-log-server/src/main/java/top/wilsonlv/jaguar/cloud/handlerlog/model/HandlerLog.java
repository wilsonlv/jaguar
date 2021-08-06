package top.wilsonlv.jaguar.cloud.handlerlog.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/08/06.
 */
@Data
@TableName("handler_log")
@EqualsAndHashCode(callSuper = false)
public class HandlerLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableField("sessionId")
    private String sessionId;
    /**
     * accessToken
     */
    @TableField("accessToken")
    private String accessToken;
    /**
     * 访问时间
     */
    @TableField("accessTime")
    private LocalDateTime accessTime;
    /**
     * 客户端IP
     */
    @TableField("clientHost")
    private String clientHost;
    /**
     * 请求地址
     */
    @TableField("requestUri")
    private String requestUri;
    /**
     * 接口操作名称
     */
    @TableField("apiOperation")
    private String apiOperation;
    /**
     * 请求参数
     */
    @TableField("parameters")
    private String parameters;
    /**
     * 请求方式
     */
    @TableField("method")
    private String method;
    /**
     * 客户端引擎
     */
    @TableField("userAgent")
    private String userAgent;
    /**
     * http响应状态码
     */
    @TableField("status")
    private Integer status;
    /**
     * 错误信息
     */
    @TableField("errorMsg")
    private String errorMsg;
    /**
     * 响应时长
     */
    @TableField("duration")
    private Long duration;
    /**
     * 创建人
     */
    @TableField("createBy")
    private String createBy;
    /**
     * 创建人ID
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

}
