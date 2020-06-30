package org.jaguar.modules.workflow.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.enums.ProcessOperationType;
import org.jaguar.modules.workflow.mapper.OperationRecordMapper;
import org.jaguar.modules.workflow.model.po.OperationRecord;
import org.jaguar.modules.workflow.service.OperationRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 工单操作记录表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Validated
@RestController
@RequestMapping("/process/operation_record")
@Api(value = "工单操作记录表管理")
public class OperationRecordController extends AbstractController<OperationRecord, OperationRecordMapper, OperationRecordService> {

    @ApiOperation(value = "查询工单操作记录表")
    @RequiresPermissions("process_operation_record_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<OperationRecord>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<OperationRecord> page,
            @ApiParam(value = "工单信息ID") @RequestParam(required = false) Long processInfoId,
            @ApiParam(value = "操作人账号") @RequestParam(required = false) String operator,
            @ApiParam(value = "操作类型") @RequestParam(required = false) ProcessOperationType processOperationType,
            @ApiParam(value = "模糊任务名称") @RequestParam(required = false) String fuzzyTaskName) {

        JaguarLambdaQueryWrapper<OperationRecord> wrapper = JaguarLambdaQueryWrapper.newInstance();
        wrapper.eq(OperationRecord::getProcessInfoId, processInfoId)
                .eq(OperationRecord::getOperator, operator)
                .eq(OperationRecord::getProcessOperationType, processOperationType)
                .like(OperationRecord::getTaskName, fuzzyTaskName);

        return super.query(page, wrapper);
    }

    @Override
    @ApiOperation(value = "工单操作记录表详情")
    @RequiresPermissions("process_operation_record_view")
    @GetMapping(value = "/{id}")
    public ResponseEntity<JsonResult<OperationRecord>> getById(@PathVariable Long id) {
        return super.getById(id);
    }

}