package org.jaguar.modules.system.mgm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;
import org.jaguar.commons.enums.ClientType;
import org.jaguar.core.base.BaseModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统登陆日志表
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Data
@TableName("t_system_login")
public class Login extends BaseModel implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    /**
     * 登录主体
     */
    @NotBlank(message = "登录主体为非空")
    @TableField("principal_")
    private String principal;
    /**
     * 登录凭证
     */
    @NotBlank(message = "登录凭证为非空")
    @TableField("credentials_")
    private String credentials;
    /**
     * 登录IP
     */
    @TableField("login_ip")
    private String loginIp;
    /**
     * 登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;
    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;
    /**
     * 响应码
     */
    @TableField("result_code")
    private Integer resultCode;
    /**
     * 客户端类型
     */
    @NotNull(message = "客户端类型为非空")
    @TableField("client_type")
    private ClientType clientType;
    /**
     * 客户端版本
     */
    @NotBlank(message = "客户端版本为非空")
    @TableField("client_version")
    private String clientVersion;
    /**
     * 设备型号
     */
    @TableField("device_model")
    private String deviceModel;
    /**
     * 设备系统版本
     */
    @TableField("device_sys_version")
    private String deviceSysVersion;
    /**
     * 设备唯一编号
     */
    @TableField("device_imei")
    private String deviceImei;

}