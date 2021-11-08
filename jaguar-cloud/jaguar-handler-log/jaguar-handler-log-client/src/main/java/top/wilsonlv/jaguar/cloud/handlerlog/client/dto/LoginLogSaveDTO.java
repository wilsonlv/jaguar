package top.wilsonlv.jaguar.cloud.handlerlog.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.web.base.BaseDTO;

import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogSaveDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

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
    private Long tenantId;

}
