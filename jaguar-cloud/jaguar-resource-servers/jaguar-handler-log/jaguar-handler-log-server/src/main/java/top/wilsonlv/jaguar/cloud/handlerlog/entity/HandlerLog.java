package top.wilsonlv.jaguar.cloud.handlerlog.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lvws
 * @since 2021/08/06.
 */
@Data
@TableName("handler_log")
@Document(indexName = "handler_log")
public class HandlerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(type = FieldType.Long)
    private Long id;
    /**
     * 会话ID
     */
    @Field(type = FieldType.Keyword)
    @TableField("sessionId")
    private String sessionId;
    /**
     * authorization
     */
    @Field(type = FieldType.Keyword)
    @TableField("authorization")
    private String authorization;
    /**
     * 访问时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_MS_PATTERN)
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DatePattern.NORM_DATETIME_MS_PATTERN)
    @TableField("accessTime")
    private Date accessTime;
    /**
     * 客户端IP
     */
    @Field(type = FieldType.Keyword)
    @TableField("clientHost")
    private String clientHost;
    /**
     * 请求地址
     */
    @Field(type = FieldType.Keyword)
    @TableField("requestUri")
    private String requestUri;
    /**
     * 接口操作名称
     */
    @Field(type = FieldType.Keyword)
    @TableField("apiOperation")
    private String apiOperation;
    /**
     * 请求参数
     */
    @Field(type = FieldType.Text)
    @TableField("parameters")
    private String parameters;
    /**
     * 请求方式
     */
    @Field(type = FieldType.Keyword)
    @TableField("method")
    private String method;
    /**
     * 客户端引擎
     */
    @Field(type = FieldType.Keyword)
    @TableField("userAgent")
    private String userAgent;
    /**
     * http响应状态码
     */
    @Field(type = FieldType.Integer)
    @TableField("status")
    private Integer status;
    /**
     * 响应结果
     */
    @Field(type = FieldType.Text)
    @TableField("jsonResult")
    private String jsonResult;
    /**
     * 错误信息
     */
    @Field(type = FieldType.Text)
    @TableField("errorMsg")
    private String errorMsg;
    /**
     * 响应时长
     */
    @Field(type = FieldType.Long)
    @TableField("duration")
    private Long duration;
    /**
     * 创建人
     */
    @Field(type = FieldType.Keyword)
    @TableField("createBy")
    private String createBy;
    /**
     * 创建人ID
     */
    @Field(type = FieldType.Keyword)
    @TableField(value = "createUserId")
    private Long createUserId;

}
