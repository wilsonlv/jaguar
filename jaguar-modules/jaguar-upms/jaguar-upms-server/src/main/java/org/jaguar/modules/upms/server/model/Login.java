package org.jaguar.modules.upms.server.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.modules.upms.enums.ClientType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel
@TableName("jaguar_modules_system_login")
@EqualsAndHashCode(callSuper = true)
public class Login extends BaseModel implements Authentication {

    private static final long serialVersionUID = 1L;

    /**
     * 登录主体
     */
    @ApiModelProperty(value = "用户账号", required = true)
    @NotBlank(message = "登录主体为非空")
    @TableField("principal_")
    private String principal;
    /**
     * 登录凭证
     */
    @ApiModelProperty(value = "登录凭证", required = true)
    @NotBlank(message = "登录凭证为非空")
    @TableField("credentials_")
    private String credentials;
    /**
     * 免密登录
     */
    @ApiModelProperty(value = "免密登录", hidden = true)
    @TableField("password_free")
    private Boolean passwordFree;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @TableField("verify_code")
    private String verifyCode;
    /**
     * 登录IP
     */
    @ApiModelProperty(hidden = true)
    @TableField("login_ip")
    private String loginIp;
    /**
     * 登录时间
     */
    @ApiModelProperty(hidden = true)
    @TableField("login_time")
    private LocalDateTime loginTime;
    /**
     * 会话ID
     */
    @ApiModelProperty(hidden = true)
    @TableField("session_id")
    private String sessionId;
    /**
     * 响应码
     */
    @ApiModelProperty(hidden = true)
    @TableField("result_code")
    private Integer resultCode;
    /**
     * 客户端类型
     */
    @ApiModelProperty(value = "客户端类型", required = true)
    @NotNull(message = "客户端类型为非空")
    @TableField("client_type")
    private ClientType clientType;
    /**
     * 客户端版本
     */
    @ApiModelProperty(value = "客户端版本", required = true)
    @NotBlank(message = "客户端版本为非空")
    @TableField("client_version")
    private String clientVersion;
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号")
    @TableField("device_model")
    private String deviceModel;
    /**
     * 设备系统版本
     */
    @ApiModelProperty(value = "设备系统版本")
    @TableField("device_sys_version")
    private String deviceSysVersion;
    /**
     * 设备唯一编号
     */
    @ApiModelProperty(value = "设备唯一编号")
    @TableField("device_imei")
    private String deviceImei;
    /**
     * 已登录的用户ID
     */
    @ApiModelProperty(hidden = true)
    @TableField("user_id")
    private Long userId;
    /**
     * 系统名称
     */
    @ApiModelProperty(hidden = true)
    @TableField("system_name")
    private String systemName;
    /**
     * 租户
     */
    @ApiModelProperty(hidden = true)
    @TableField("tenant_")
    private String tenant;


    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Set<GrantedAuthority> authorities;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private boolean authenticated;

    @Override
    public Object getDetails() {
        return this;
    }

    @Override
    public String getName() {
        return this.principal;
    }
}