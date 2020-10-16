package org.jaguar.modules.workflow.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.mapper.ProcessRemarkMapper;
import org.jaguar.modules.workflow.model.po.ProcessRemark;
import org.jaguar.modules.workflow.service.ProcessRemarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 工单备注表
 *
 * @author lvws
 * @date 2020-04-25 17:06:48
 */
@Validated
@RestController
@RequestMapping("/process/process_remark")
@Api(tags = "工单备注表管理")
public class ProcessRemarkController extends AbstractController<ProcessRemark, ProcessRemarkMapper, ProcessRemarkService> {

    @ApiOperation(value = "查询工单备注表")
    @GetMapping("/page")
    public ResponseEntity<JsonResult<Page<ProcessRemark>>> page(
            @ApiIgnore com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProcessRemark> page,
            @ApiParam(value = "工单ID", required = true) @RequestParam Long processInfoId) {

        JaguarLambdaQueryWrapper<ProcessRemark> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.eq(ProcessRemark::getProcessInfoId, processInfoId);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "工单备注表详情")
    @GetMapping("/{id}")
    public ResponseEntity<JsonResult<ProcessRemark>> detail(@PathVariable("id") Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "新增工单备注表")
    @PostMapping(value = "/insert")
    public ResponseEntity<JsonResult<ProcessRemark>> save(@Valid @RequestBody ProcessRemark processRemark) {

        processRemark.setRemarkUser(getCurrentUserAccount());
        processRemark.setRemarkTime(LocalDateTime.now());
        return super.insert(processRemark);
    }
}
