package top.wilsonlv.jaguar.cloud.upms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.DeptCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.DeptModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.Dept;
import top.wilsonlv.jaguar.cloud.upms.mapper.DeptMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.DeptVO;
import top.wilsonlv.jaguar.cloud.upms.service.DeptService;
import top.wilsonlv.jaguar.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 部门  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2021-11-15
 */
@Validated
@RestController
@RequestMapping("/admin/dept")
@Api(tags = "部门管理")
public class DeptController extends BaseController<Dept, DeptMapper, DeptService> {

    @ApiOperation(value = "查询部门树")
    @PreAuthorize("hasAuthority('部门管理')")
    @GetMapping(value = "/tree")
    public JsonResult<List<DeptVO>> tree() {
        return success(service.tree(0));
    }

    @ApiOperation(value = "部门详情")
    @PreAuthorize("hasAuthority('部门管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<DeptVO> detail(@PathVariable Long id) {
        return success(service.getDetail(id));
    }

    @ApiOperation(value = "新增部门")
    @PreAuthorize("hasAuthority('部门管理')")
    @PostMapping
    public JsonResult<Void> create(@RequestBody @Valid DeptCreateDTO dept) {
        service.create(dept);
        return success();
    }

    @ApiOperation(value = "修改部门")
    @PreAuthorize("hasAuthority('部门管理')")
    @PutMapping
    public JsonResult<Void> modify(@RequestBody @Valid DeptModifyDTO dept) {
        service.modify(dept);
        return success();
    }

    @ApiOperation(value = "删除部门")
    @PreAuthorize("hasAuthority('部门管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAndDelete(id);
        return success();
    }

}