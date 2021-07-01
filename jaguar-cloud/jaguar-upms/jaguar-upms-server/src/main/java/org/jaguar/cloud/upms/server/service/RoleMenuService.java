package org.jaguar.cloud.upms.server.service;

import org.jaguar.cloud.upms.server.model.RoleMenu;
import org.jaguar.commons.basecrud.BaseService;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.cloud.upms.sdk.dto.MenuFunction;
import org.jaguar.cloud.upms.server.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserRoleService userRoleService;


    /*---------- 个人用户菜单权限查询 ----------*/



    /*---------- 权限管理 ----------*/

    /**
     * 关联菜单和功能
     *
     * @param roleId            角色ID
     * @param menuFunctionNames 菜单功能名称集合
     */
    @Transactional
    public void relateMenuFunctions(Long roleId, Set<String> menuFunctionNames) {
        for (String menuFunctionName : menuFunctionNames) {
            if (!MenuFunction.hasName(menuFunctionName)) {
                throw new CheckedException("无效的菜单或功能名称");
            }

            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuFuncionName(menuFunctionName);

            if (this.exists(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                    .setEntity(roleMenu))) {
                continue;
            }

            this.insert(roleMenu);
        }

        this.delete(JaguarLambdaQueryWrapper.<RoleMenu>newInstance()
                .eq(RoleMenu::getRoleId, roleId)
                .notIn(RoleMenu::getMenuFuncionName, menuFunctionNames));
    }

}
