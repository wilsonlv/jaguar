package org.jaguar.modules.system.mgm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.system.mgm.mapper.MenuMapper;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统菜单表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Validated
@RestController
@RequestMapping("/system/menu")
@Api(value = "系统菜单表管理", description = "系统菜单表管理")
public class MenuController extends AbstractController<Menu, MenuMapper, MenuService> {


    @ApiOperation(value = "查询系统菜单表")
    @RequiresPermissions("系统菜单表:读取")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<Menu>>> page(
        @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<Menu> page,
        @ApiParam(value = "查询信息") Menu menu) {

        LambdaQueryWrapper<Menu> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(menu);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "系统菜单表详情")
    @RequiresPermissions("系统菜单表:读取")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<Menu>> detail(@PathVariable Long id){
        return super.getById(id);
    }

    @ApiOperation(value = "修改系统菜单表")
    @RequiresPermissions("系统菜单表:新增编辑")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult<Menu>> update(@RequestBody @NotNull Menu menu){
        return super.saveOrUpdate(menu);
    }

    @ApiOperation(value = "删除系统菜单表")
    @RequiresPermissions("系统菜单表:删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity del(@PathVariable Long id){
        return super.delete(id);
    }

}