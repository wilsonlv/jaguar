package top.wilsonlv.jaguar.cloud.upms.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.mapper.UserMapper;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.cloud.upms.service.UserRoleService;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.data.encryption.util.EncryptionUtil;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

/**
 * @author lvws
 * @since 2021/4/8
 */
@Service
public class PersonalApi extends BaseService<User, UserMapper> {

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

}
