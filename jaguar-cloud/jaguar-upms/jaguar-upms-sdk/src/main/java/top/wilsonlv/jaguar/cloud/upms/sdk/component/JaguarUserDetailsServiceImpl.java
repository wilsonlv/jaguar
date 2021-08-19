package top.wilsonlv.jaguar.cloud.upms.sdk.component;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityAuthority;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/6/30
 */
@Component
@RequiredArgsConstructor
public class JaguarUserDetailsServiceImpl implements UserDetailsService {

    private final RemoteUserService remoteUserService;

    private final ClientDetailsService clientDetailsService;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(SecurityUtil.getClientId());
        Map<String, Object> additionalInformation = clientDetails.getAdditionalInformation();
        String userTypeStr = (String) additionalInformation.get("userType");
        if (StringUtils.isBlank(userTypeStr)) {
            throw new CheckedException("当前客户端没有配置用户类型");
        }

        UserType userType = UserType.valueOf(userTypeStr);
        switch (userType) {
            case ADMIN: {
                return getAdminUser(username);
            }
            case TENANT:
            case USER: {
                return null;
            }
            default:
                return null;
        }
    }

    private SecurityUser getAdminUser(String username) {
        UserVO user = remoteUserService.loadUserByUsername(username);
        if (user == null) {
            return null;
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(user.getId());
        securityUser.setUsername(user.getUserAccount());
        securityUser.setPassword(user.getUserPassword());
        securityUser.setUserType(UserType.ADMIN);
        securityUser.setPhone(user.getUserPhone());
        securityUser.setEmail(user.getUserEmail());
        securityUser.setLocked(user.getUserLocked());
        securityUser.setEnable(user.getUserEnable());
        securityUser.setPasswordLastModifyTime(user.getUserPasswordLastModifyTime());
//        securityUser.setAuthorities();
        return securityUser;
    }

}
