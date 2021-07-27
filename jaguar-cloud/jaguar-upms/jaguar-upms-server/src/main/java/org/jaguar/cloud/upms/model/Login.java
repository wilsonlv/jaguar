package org.jaguar.cloud.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.cloud.upms.sdk.enums.UserType;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.cloud.upms.sdk.enums.ClientType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 * 系统登录日志表
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Data
@Document(indexName = "login_log")
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    private Long id;
    /**
     * 登录主体
     */
    private String principal;
    /**
     * 登录凭证
     */
    private String credentials;
    /**
     * 免密登录
     */
    private Boolean passwordFree;
    /**
     * 已登录的用户ID
     */
    private Long loginUserId;
    /**
     * 用户类型
     */
    private UserType loginUserType;
    /**
     * 登录IP
     */
    private String loginIp;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * accessToken
     */
    private String accessToken;
    /**
     * 是否登录成功
     */
    private Boolean loginSuccess;
    /**
     * 错误信息
     */
    private Boolean loginErrorMsg;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端类型
     */
    private ClientType clientType;
    /**
     * 客户端版本
     */
    private String clientVersion;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 设备系统版本
     */
    private String deviceSysVersion;
    /**
     * 设备唯一编号
     */
    private String deviceImei;
    /**
     * 租户
     */
    private String tenantId;

}