package top.wilsonlv.jaguar.cloud.upms.auth;


import org.apache.commons.lang3.StringUtils;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserMapper;
import top.wilsonlv.jaguar.cloud.upms.model.Role;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.cloud.upms.service.UserRoleService;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Service
public class AuthService extends BaseService<User, UserMapper> implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;


    @Transactional
    public void modifyPassword(Long currentUser, String oldPassword, String newPassword) {
        User user = this.getById(currentUser);

        if (!user.getUserPassword().equals(oldPassword)) {
            throw new CheckedException("密码错误");
        }

        if (EncryptionUtil.passwordUnmatched(newPassword)) {
            throw new CheckedException("密码格式为包含数字，字母大小写的6-20位字符串！");
        }

        user = new User();
        user.setId(currentUser);
        user.setUserPassword(newPassword);
        this.updateById(user);
    }

    /**
     * security 登录时调用
     *
     * @param username 用户账号
     * @return 用户
     * @throws UsernameNotFoundException 用户名和密码错误
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(null);
        }

//        return this.getDetail(user);
        return null;
    }
}
