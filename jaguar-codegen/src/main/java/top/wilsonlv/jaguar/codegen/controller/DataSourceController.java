package top.wilsonlv.jaguar.codegen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.wilsonlv.jaguar.codegen.mapper.DataSourceMapper;
import top.wilsonlv.jaguar.codegen.model.DataSource;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.codegen.service.DataSourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author lvws
 * @since 2021-06-16
 */
@Validated
@RestController
@RequestMapping("/datasource")
@Api(tags = "数据源管理")
public class DataSourceController extends BaseController<DataSource, DataSourceMapper, DataSourceService> {

    @ApiOperation(value = "分页查询数据源")
    @GetMapping(value = "/page")
    public JsonResult<Page<DataSource>> page(@ApiIgnore Page<DataSource> page,
                                             @ApiParam(value = "模糊数据源名称") @RequestParam(required = false) String fuzzyName) {

        LambdaQueryWrapper<DataSource> wrapper = JaguarLambdaQueryWrapper.<DataSource>newInstance()
                .like(DataSource::getName, fuzzyName);

        page = service.query(page, wrapper);
        return success(page);
    }

    @ApiOperation(value = "新增修改数据源")
    @PostMapping
    public JsonResult<Void> save(@RequestBody @Valid DataSource dataSource) {
        synchronized (this) {
            service.save(dataSource);
        }
        return success();
    }

    @ApiOperation(value = "删除数据源")
    @DeleteMapping("/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.del(id);
        return success();
    }

}