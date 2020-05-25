package org.jaguar.modules.system.mgm.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.system.mgm.mapper.MenuMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.model.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Service
public class MenuService extends BaseService<Menu, MenuMapper> {

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 查询菜单树
     *
     * @return 菜单树
     */
    @Transactional
    public List<Menu> tree() {
        List<Menu> menuList = this.list(JaguarLambdaQueryWrapper.<Menu>newInstance()
                .isNull(Menu::getMenuParentId)
                .orderByAsc(Menu::getMenuSortNo));
        for (Menu menu : menuList) {
            recursionChildren(menu);
        }
        return menuList;
    }

    /**
     * 递归查询子菜单
     *
     * @param parent 父菜单
     */
    private void recursionChildren(Menu parent) {
        List<Menu> menuList = this.list(JaguarLambdaQueryWrapper.<Menu>newInstance()
                .eq(Menu::getMenuParentId, parent.getId())
                .orderByAsc(Menu::getMenuSortNo));
        parent.setChildren(menuList);

        for (Menu menu : menuList) {
            recursionChildren(menu);
        }
    }

    @Transactional
    public Menu createOrUpdate(Menu menu) {
        if (menu.getMenuParentId() != null) {
            Menu parent = this.getById(menu.getMenuParentId());
            Assert.validateId(parent, "父菜单", menu.getMenuParentId());
        }
        return this.saveOrUpdate(menu);
    }

    @Transactional
    public void deleteWithRoleMenuId(Long id) {
        if (this.exists(JaguarLambdaQueryWrapper.<Menu>newInstance().eq(Menu::getMenuParentId, id))) {
            throw new CheckedException("该菜单下挂有子菜单，不可删除！");
        }

        this.delete(id);
        roleMenuService.delete(JaguarLambdaQueryWrapper.<RoleMenu>newInstance().eq(RoleMenu::getMenuId, id));
    }


}
