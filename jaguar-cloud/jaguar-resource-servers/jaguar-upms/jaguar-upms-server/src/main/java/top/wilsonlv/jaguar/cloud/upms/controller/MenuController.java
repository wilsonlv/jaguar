package top.wilsonlv.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.MenuCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.MenuModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.ResourceServer;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.mapper.MenuMapper;
import top.wilsonlv.jaguar.cloud.upms.entity.Menu;
import top.wilsonlv.jaguar.cloud.upms.service.MenuService;
import top.wilsonlv.jaguar.cloud.upms.service.ResourceServerService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.base.BaseVO;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @ApiOperation(value = "查询菜单树")
    @PreAuthorize("hasAuthority('菜单管理')")
    @GetMapping(value = "/tree")
    public JsonResult<List<MenuVO>> tree() {
        return success(service.treeAllResourceServerMenu());
    }

    @ApiOperation(value = "菜单详情")
    @PreAuthorize("hasAuthority('菜单管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<MenuVO> detail(@PathVariable Long id) {
        Menu menu = service.getById(id);
        return success(menu.toVo(MenuVO.class));
    }

    @ApiOperation(value = "新增菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @PostMapping
    public JsonResult<Void> create(@Valid @RequestBody MenuCreateDTO menu) {
        service.create(menu);
        return success();
    }

    @ApiOperation(value = "修改菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @PutMapping
    public JsonResult<Void> modify(@Valid @RequestBody MenuModifyDTO menu) {
        service.modify(menu);
        return success();
    }

    @ApiOperation(value = "删除菜单")
    @PreAuthorize("hasAuthority('菜单管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAndDelete(id);
        return success();
    }

}