package com.jaguar.process.controller;

import com.jaguar.core.util.InstanceUtil;
import com.jaguar.web.JsonResult;
import com.jaguar.web.base.AbstractController;
import com.jaguar.process.model.po.ProcessOperationRecord;
import com.jaguar.process.service.ProcessOperationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.jaguar.core.constant.Constant.*;

/**
 * <p>
 * 流程操作记录表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@RestController
@Api(value = "流程操作记录表管理", description = "流程操作记录表管理")
@RequestMapping("/process_operation_record")
public class ProcessOperationRecordController extends AbstractController<ProcessOperationRecordService> {

    @ApiOperation(value = "查询流程操作记录表")
    @RequiresPermissions("processOperationRecord:read")
    @GetMapping(value = "/list")
    public ResponseEntity<JsonResult> query(
                        @ApiParam(value = "页码") @RequestParam(required = false) Integer page,
                        @ApiParam(value = "页量") @RequestParam(required = false) Integer rows,
                        @ApiParam(value = "工单信息ID") @RequestParam(required = false) Long processInfoId) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, page);
        param.put(ROWS, rows);

        param.put("processInfoId", processInfoId);

        return super.query( param);
    }

    @ApiOperation(value = "流程操作记录表详情")
    @RequiresPermissions("processOperationRecord:read")
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<JsonResult> get( @PathVariable Long id) {
        return super.get( id);
    }

    @ApiOperation(value = "修改流程操作记录表")
    @RequiresPermissions("processOperationRecord:update")
    @PostMapping(value = "/update")
    public ResponseEntity<JsonResult> update( ProcessOperationRecord param) {
        return super.update( param);
    }

    @ApiOperation(value = "删除流程操作记录表")
    @RequiresPermissions("processOperationRecord:update")
    @DeleteMapping(value = "/del/{id}")
    public ResponseEntity<JsonResult> del( @PathVariable Long id) {
        return super.del( id);
    }

    @ApiOperation(value = "删除流程操作记录表")
    @RequiresPermissions("processOperationRecord:update")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<JsonResult> delete( @PathVariable Long id) {
        return super.delete( id);
    }
}