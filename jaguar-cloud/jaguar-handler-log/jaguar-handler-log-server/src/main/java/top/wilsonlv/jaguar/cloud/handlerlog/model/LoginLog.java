package top.wilsonlv.jaguar.cloud.handlerlog.model;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.enums.UserType;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统登录日志表
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Data
@TableName("login_log")
@Document(indexName = "login_log")
@EqualsAndHashCode(callSuper = false)
public class LoginLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 登录主体
     */
    @TableField("principal")
    private String principal;
    /**
     * 登录凭证
     */
    @TableField("credentials")
    private String credentials;
    /**
     * 免密登录
     */
    @TableField("passwordFree")
    private Boolean passwordFree;
    /**
     * 已登录的用户ID
     */
    @TableField("loginUserId")
    private Long loginUserId;
    /**
     * 用户类型
     */
    @TableField("loginUserType")
    private UserType loginUserType;
    /**
     * 登录IP
     */
    @TableField("loginIp")
    private String loginIp;
    /**
     * 登录时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField("loginTime")
    private LocalDateTime loginTime;
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
     * 是否登录成功
     */
    @TableField("loginSuccess")
    private Boolean loginSuccess;
    /**
     * 错误信息
     */
    @TableField("loginErrorMsg")
    private Boolean loginErrorMsg;
    /**
     * 客户端ID
     */
    @TableField("clientId")
    private String clientId;
    /**
     * 客户端类型
     */
    @TableField("clientType")
    private ClientType clientType;
    /**
     * 客户端版本
     */
    @TableField("clientVersion")
    private String clientVersion;
    /**
     * 设备型号
     */
    @TableField("deviceModel")
    private String deviceModel;
    /**
     * 设备系统版本
     */
    @TableField("deviceSysVersion")
    private String deviceSysVersion;
    /**
     * 设备唯一编号
     */
    @TableField("deviceImei")
    private String deviceImei;
    /**
     * 租户
     */
    @TableField("tenantId")
    private Long tenantId;

}