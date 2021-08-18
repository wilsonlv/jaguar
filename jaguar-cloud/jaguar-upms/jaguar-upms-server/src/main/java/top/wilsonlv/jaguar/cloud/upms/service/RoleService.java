package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.mapper.RoleMapper;
import top.wilsonlv.jaguar.cloud.upms.entity.Role;
import top.wilsonlv.jaguar.cloud.upms.entity.RoleMenu;
import top.wilsonlv.jaguar.cloud.upms.entity.UserRole;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class RoleService extends BaseService<Role, RoleMapper> {

    @Lazy
    @Autowired
    private UserRoleService userRoleService;
    @Lazy
    @Autowired
    private RoleMenuService roleMenuService;


    public Role getByRoleName(String roleName) {
        return this.unique(JaguarLambdaQueryWrapper.<Role>newInstance()
                .eq(Role::getRoleName, roleName));
    }

    public RoleVO getDetail(Long roleId) {
        Role role = this.getById(roleId);

        List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.lambdaQuery(RoleMenu.class)
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, roleId));
        Set<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());

        RoleVO roleVO = new RoleVO(role.getRoleName(), role.getRoleBuiltIn(), role.getRoleEnable());
        roleVO.setMenuIds(menuIds);
        return roleVO;
    }

    @Transactional
    public Page<RoleVO> queryWithUser(Page<Role> page, LambdaQueryWrapper<Role> wrapper) {
        page = this.query(page, wrapper);

        Page<RoleVO> roles = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        for (Role role : page.getRecords()) {
            List<UserVO> users = userRoleService.listUserByRoleId(role.getId());

            RoleVO roleVO = new RoleVO(role.getRoleName(), role.getRoleBuiltIn(), role.getRoleEnable());
            roleVO.setUsers(users);
            roles.getRecords().add(roleVO);
        }
        return roles;
    }

    @Klock(name = LockNameConstant.ROLE_CREATE_MODIFY_LOCK)
    @Transactional
    public void create(RoleCreateDTO roleCreateDTO) {
        Role unique = this.getByRoleName(roleCreateDTO.getRoleName());
        Assert.duplicate(unique, "角色名称");

        Role role = new Role();
        role.setRoleName(roleCreateDTO.getRoleName());
        role.setRoleEnable(roleCreateDTO.getRoleEnable());
        role.setRoleBuiltIn(false);
        this.insert(role);

        roleMenuService.relateMenus(role.getId(), roleCreateDTO.getMenuIds());
    }

    @Klock(name = LockNameConstant.ROLE_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(RoleModifyDTO roleModifyDTO) {
        this.getById(roleModifyDTO.getId());

        if (StringUtils.isNotBlank(roleModifyDTO.getRoleName())) {
            Role byRoleName = this.getByRoleName(roleModifyDTO.getRoleName());
            Assert.duplicate(byRoleName, roleModifyDTO, "角色名称");
        }

        Role role = new Role();
        role.setId(roleModifyDTO.getId());
        role.setRoleName(roleModifyDTO.getRoleName());
        role.setRoleEnable(roleModifyDTO.getRoleEnable());
        this.updateById(role);
    }

    @Transactional
    public void checkAndDelete(Long id) {
        if (userRoleService.exists(Wrappers.lambdaUpdate(UserRole.class)
                .eq(UserRole::getRoleId, id))) {
            throw new CheckedException("该角色下已绑定用户，不可删除！");
        }

        this.delete(id);
    }


}
