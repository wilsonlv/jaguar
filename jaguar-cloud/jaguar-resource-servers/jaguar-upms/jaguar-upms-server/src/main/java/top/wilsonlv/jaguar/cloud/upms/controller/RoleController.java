package top.wilsonlv.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.basecrud.BaseController;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.RoleModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.Role;
import top.wilsonlv.jaguar.cloud.upms.mapper.RoleMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.RoleVO;
import top.wilsonlv.jaguar.cloud.upms.service.RoleService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

import javax.validation.Valid;

/**
 * <p>
 * 角色  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2021-08-17
 */
@Validated
@RestController
@RequestMapping("/admin/role")
@Api(tags = "角色管理")
public class RoleController extends BaseController<Role, RoleMapper, RoleService> {

    @ApiOperation(value = "查询角色")
    @PreAuthorize("hasAuthority('角色管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<RoleVO>> page(
            @ApiIgnore Page<Role> page,
            @ApiParam(value = "模糊角色名称") @RequestParam(required = false) String fuzzyRoleName,
            @ApiParam(value = "角色是否启用") @RequestParam(required = false) Boolean roleEnable) {

        LambdaQueryWrapper<Role> wrapper = JaguarLambdaQueryWrapper.<Role>newInstance().
                like(Role::getRoleName, fuzzyRoleName)
                .eq(Role::getRoleEnable, roleEnable);

        return success(service.queryWithUser(page, wrapper));
    }

    @ApiOperation(value = "角色详情")
    @PreAuthorize("hasAuthority('角色管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<RoleVO> detail(@PathVariable Long id) {

        RoleVO role = service.getDetail(id);
        return success(role);
    }

    @ApiOperation(value = "新增角色")
    @PreAuthorize("hasAuthority('角色管理')")
    @PostMapping
    public JsonResult<Void> create(@Valid @RequestBody RoleCreateDTO role) {
        service.create(role);
        return success();
    }

    @ApiOperation(value = "修改角色")
    @PreAuthorize("hasAuthority('角色管理')")
    @PutMapping
    public JsonResult<Void> modify(@Valid @RequestBody RoleModifyDTO role) {
        service.modify(role);
        return success();
    }

    @ApiOperation(value = "删除角色")
    @PreAuthorize("hasAuthority('角色管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAndDelete(id);
        return success();
    }

}