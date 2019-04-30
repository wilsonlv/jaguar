package com.jaguar.mybatisplus.generator.controller;

import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.mybatisplus.generator.model.TableInfo;
import com.jaguar.mybatisplus.generator.service.TableInfoService;
import com.jaguar.web.JsonResult;
import com.jaguar.web.base.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.jaguar.core.constant.Constant.*;

/**
 * Created by lvws on 2019/4/30.
 */
@RestController
@Api(value = "系统配置表管理", description = "系统配置表管理")
@RequestMapping("/table_info")
public class TableInfoController extends AbstractController<TableInfoService> {

    @ApiOperation(value = "查询系统配置表")
    @RequiresPermissions("tableInfo:read")
    @GetMapping(value = "/show_tables")
    public ResponseEntity<JsonResult> showTables(
            @ApiParam(value = "页码") @RequestParam(required = false) Integer page,
            @ApiParam(value = "页量") @RequestParam(required = false) Integer rows,
            @ApiParam(value = "模糊表名") @RequestParam(required = false) Integer fuzzyTableName) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, page);
        param.put(ROWS, rows);
        param.put(SORT, "table_name");
        param.put(ORDER, OrderType.ASC);

        param.put("fuzzyTableName", fuzzyTableName);

        List<TableInfo> tableInfos = service.showTables(param);
        return setSuccessJsonResult(tableInfos);
    }

    @ApiOperation(value = "生成代码")
    @RequiresPermissions("tableInfo:update")
    @PostMapping(value = "/generate")
    public ResponseEntity<JsonResult> generate(
            @ApiParam(value = "表名", required = true) @RequestParam String tableName,
            @ApiParam(value = "包名", required = true) @RequestParam String parentPackage,
            @ApiParam(value = "作者", required = true) @RequestParam String author,
            @ApiParam(value = "文件输出目录", required = true) @RequestParam String outputDir) {

        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setParentPackage(parentPackage);
        tableInfo.setAuthor(author);
        tableInfo.setOutputDir(outputDir);

        service.generate(tableInfo);
        return setSuccessJsonResult();
    }

}
