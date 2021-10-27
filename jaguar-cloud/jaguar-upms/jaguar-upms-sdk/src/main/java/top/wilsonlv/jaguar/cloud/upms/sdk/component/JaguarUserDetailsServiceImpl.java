package top.wilsonlv.jaguar.cloud.upms.sdk.component;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.enums.UserType;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityAuthority;
import top.wilsonlv.jaguar.commons.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.commons.oauth2.util.SecurityUtil;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        JsonResult<UserVO> result = remoteUserService.loadUserByUsername(username);
        if (result.getData() == null) {
            throw new UsernameNotFoundException(null);
        }

        UserVO user = result.getData();
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

        Set<SecurityAuthority> authorities = new HashSet<>(user.getPermissions().size() + 1);
        securityUser.setAuthorities(authorities);
        authorities.add(new SecurityAuthority("ROLE_" + UserType.ADMIN));
        for (String permission : user.getPermissions()) {
            authorities.add(new SecurityAuthority(permission));
        }
        return securityUser;
    }

}
