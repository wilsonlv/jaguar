package top.wilsonlv.jaguar.cloud.upms.service;

import org.springframework.stereotype.Service;
import top.wilsonlv.jaguar.cloud.upms.mapper.RoleMenuMapper;
import top.wilsonlv.jaguar.cloud.upms.model.RoleMenu;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;

import java.util.Set;

/**
 * <p>
 * 系统角色菜单表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class RoleMenuService extends BaseService<RoleMenu, RoleMenuMapper> {


    public Set<String> listPermissionsByRoleIds(Set<Long> roleIds) {
        return this.mapper.listPermissionsByRoleIds(roleIds);
    }


    /*---------- 个人用户菜单权限查询 ----------*/



    /*---------- 权限管理 ----------*/


}
