package top.wilsonlv.jaguar.cloud.upms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wilsonlv.jaguar.cloud.upms.constant.LockNameConstant;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.MenuCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.MenuModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.mapper.MenuMapper;
import top.wilsonlv.jaguar.cloud.upms.entity.Menu;
import top.wilsonlv.jaguar.commons.basecrud.Assert;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2021-08-17
 */
@Service
public class MenuService extends BaseService<Menu, MenuMapper> {

    /*----------  通用接口  ----------*/

    public MenuVO model2Vo(Menu menu) {
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }

    public Menu getByMenuName(String menuName) {
        return this.unique(Wrappers.lambdaQuery(Menu.class)
                .eq(Menu::getMenuName, menuName));
    }

    public Menu getByMenuPermission(String menuPermission) {
        return this.unique(Wrappers.lambdaQuery(Menu.class)
                .eq(Menu::getMenuPermission, menuPermission));
    }

    /*---------- 菜单管理 ----------*/

    @Transactional
    public List<MenuVO> tree(Long parentId) {
        List<Menu> menus;
        if (parentId == null) {
            menus = this.list(Wrappers.lambdaQuery(Menu.class)
                    .isNull(Menu::getParentId));
        } else {
            menus = this.list(Wrappers.lambdaQuery(Menu.class)
                    .eq(Menu::getParentId, parentId));
        }

        List<MenuVO> menuVos = new ArrayList<>(menus.size());
        for (Menu menu : menus) {
            MenuVO menuVO = this.model2Vo(menu);
            menuVO.setChildren(this.tree(menu.getId()));
            menuVos.add(menuVO);
        }
        return menuVos;
    }

    @Klock(name = LockNameConstant.MENU_CREATE_MODIFY_LOCK)
    @Transactional
    public void create(MenuCreateDTO menuCreateDTO) {
        Menu byMenuName = this.getByMenuName(menuCreateDTO.getMenuName());
        Assert.duplicate(byMenuName, "名称");

        Menu byMenuPermission = this.getByMenuPermission(menuCreateDTO.getMenuPermission());
        Assert.duplicate(byMenuPermission, "权限");

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuCreateDTO, menu);
        this.insert(menu);
    }

    public void checkBuiltIn(Long id){
        Menu byId = this.getById(id);
        if (byId.getMenuBuiltIn()) {
            throw new CheckedException("内置菜单不可修改");
        }
    }

    @Klock(name = LockNameConstant.MENU_CREATE_MODIFY_LOCK)
    @Transactional
    public void modify(MenuModifyDTO menuModifyDTO) {
        this.checkBuiltIn(menuModifyDTO.getId());

        if (StringUtils.isNotBlank(menuModifyDTO.getMenuName())) {
            Menu byMenuName = this.getByMenuName(menuModifyDTO.getMenuName());
            Assert.duplicate(byMenuName, menuModifyDTO, "名称");
        }

        if (StringUtils.isNotBlank(menuModifyDTO.getMenuPermission())) {
            Menu byMenuPermission = this.getByMenuPermission(menuModifyDTO.getMenuPermission());
            Assert.duplicate(byMenuPermission, "权限");
        }

        Menu menu = new Menu();
        BeanUtils.copyProperties(menuModifyDTO, menu);
        this.updateById(menu);
    }

    @Transactional
    public void checkAndDelete(Long id) {
        this.checkBuiltIn(id);
        this.delete(id);
    }
}