package top.wilsonlv.jaguar.cloud.upms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.mapper.MenuMapper;
import top.wilsonlv.jaguar.cloud.upms.model.Menu;
import top.wilsonlv.jaguar.cloud.upms.service.MenuService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 菜单  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2021-08-17
 */
@Validated
@RestController
@RequestMapping("/admin/menu")
@Api(tags = "菜单管理")
public class MenuController extends BaseController<Menu, MenuMapper, MenuService> {

    @ApiOperation(value = "查询菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @GetMapping(value = "/tree")
    public JsonResult<List<MenuVO>> tree() {
        return null;
    }

    @ApiOperation(value = "菜单详情")
    @PreAuthorize("hasAuthority('菜单管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<MenuVO> detail(@PathVariable Long id) {

//        MenuVO role = service.getDetail(id);
        return null;
    }

    @ApiOperation(value = "新建菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @PostMapping
    public JsonResult<Void> create(@Valid @RequestBody RoleCreateDTO role) {
//        service.create(role);
        return success();
    }

    @ApiOperation(value = "修改菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @PostMapping
    public JsonResult<Void> modify(@Valid @RequestBody RoleModifyDTO role) {
//        service.modify(role);
        return success();
    }

    @ApiOperation(value = "删除菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<?> del(@PathVariable Long id) {
//        service.checkAndDelete(id);
        return success();
    }

}