package top.wilsonlv.jaguar.cloud.upms.sdk.component;

import lombok.RequiredArgsConstructor;
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

import java.util.HashSet;

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
        String clientId = SecurityUtil.getClientId();
        if (clientId == null) {
            return getAdminUser(username);
        }

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        UserType userType = null;
        outer:
        for (String scope : clientDetails.getScope()) {
            for (UserType item : UserType.values()) {
                if (item.getUserTypeName().equals(scope)) {
                    userType = item;
                    break outer;
                }
            }
        }
        if (userType == null) {
            throw new UsernameNotFoundException(null);
        }

        switch (userType) {
            case ADMIN: {
                return getAdminUser(username);
            }
            case TENANT: {
                return null;
            }
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
        securityUser.setLocked(user.getUserLocked());
        securityUser.setEnable(user.getUserEnable());
        securityUser.setPasswordLastModifyTime(user.getUserPasswordLastModifyTime());

        securityUser.setAuthorities(new HashSet<>(user.getPermissions().size()));
        for (String permission : user.getPermissions()) {
            securityUser.getAuthorities().add(new SecurityAuthority(permission));
        }
        return securityUser;
    }

}
