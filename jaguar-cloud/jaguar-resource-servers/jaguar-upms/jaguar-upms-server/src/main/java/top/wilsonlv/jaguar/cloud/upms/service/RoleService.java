package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.Role;
import top.wilsonlv.jaguar.cloud.upms.entity.RoleMenu;
import top.wilsonlv.jaguar.cloud.upms.entity.UserRole;
import top.wilsonlv.jaguar.cloud.upms.mapper.RoleMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleMenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.basecrud.Assert;
import top.wilsonlv.jaguar.rediscache.AbstractRedisCacheService;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class RoleService extends AbstractRedisCacheService<Role, RoleMapper> {

    @Lazy
    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleMenuService roleMenuService;


    public Role getByRoleName(String roleName) {
        return this.unique(Wrappers.lambdaQuery(Role.class)
                .eq(Role::getRoleName, roleName));
    }

    public RoleVO getDetail(Long roleId) {
        Role role = this.getById(roleId);
        RoleVO roleVO = role.toVo(RoleVO.class);

        List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.lambdaQuery(RoleMenu.class)
                .eq(RoleMenu::getRoleId, roleId));
        roleVO.setRoleMenus(new ArrayList<>(roleMenus.size()));

        for (RoleMenu roleMenu : roleMenus) {
            RoleMenuVO roleMenuVO = roleMenu.toVo(RoleMenuVO.class);
            roleVO.getRoleMenus().add(roleMenuVO);
        }
        return roleVO;
    }

    @Transactional
    public Page<RoleVO> queryWithUser(Page<Role> page, LambdaQueryWrapper<Role> wrapper) {
        page = this.query(page, wrapper);
        Page<RoleVO> voPage = this.toVoPage(page);

        for (Role role : page.getRecords()) {
            RoleVO roleVO = role.toVo(RoleVO.class);
            voPage.getRecords().add(roleVO);

            List<UserVO> users = userRoleService.listUserByRoleId(role.getId());
            roleVO.setUsers(users);
        }
        return voPage;
    }

    @Klock(name = LockNameConstant.ROLE_CREATE_MODIFY_LOCK)
    @Transactional
    public void create(RoleCreateDTO roleCreateDTO) {
        Role unique = this.getByRoleName(roleCreateDTO.getRoleName());
        Assert.duplicate(unique, "角色名称");

        Role role = new Role();
        role.setRoleBuiltIn(false);
        role.setRoleName(roleCreateDTO.getRoleName());
        role.setRoleEnable(roleCreateDTO.getRoleEnable());
        this.insert(role);

        roleMenuService.relateMenus(role.getId(), roleCreateDTO.getMenuIds());
    }

    @Klock(name = LockNameConstant.ROLE_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(RoleModifyDTO roleModifyDTO) {
        Role byRoleName = this.getByRoleName(roleModifyDTO.getRoleName());
        Assert.duplicate(byRoleName, roleModifyDTO, "角色名称");

        Role role = roleModifyDTO.toEntity(Role.class);
        this.updateById(role);

        roleMenuService.relateMenus(role.getId(), roleModifyDTO.getMenuIds());
    }

    @Transactional
    public void checkAndDelete(Long id) {
        if (userRoleService.exists(Wrappers.lambdaUpdate(UserRole.class)
                .eq(UserRole::getRoleId, id))) {
            throw new CheckedException("该角色下已绑定用户，不可删除！");
        }

        Role role = this.getById(id);
        if (role.getRoleBuiltIn()) {
            throw new CheckedException("内置脚色不可刪除");
        }

        this.delete(id);
    }

}
