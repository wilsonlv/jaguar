package org.jaguar.modules.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.core.web.Page;
import org.jaguar.modules.workflow.enums.ButtonPosition;
import org.jaguar.modules.workflow.enums.TaskStatus;
import org.jaguar.modules.workflow.mapper.ProcessInfoMapper;
import org.jaguar.modules.workflow.model.dto.FormDataDTO;
import org.jaguar.modules.workflow.model.po.ButtonDef;
import org.jaguar.modules.workflow.model.po.OperationRecord;
import org.jaguar.modules.workflow.model.po.ProcessInfo;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.model.vo.ProcessView;
import org.jaguar.modules.workflow.service.ButtonDefService;
import org.jaguar.modules.workflow.service.FlowDefinitionService;
import org.jaguar.modules.workflow.service.OperationRecordService;
import org.jaguar.modules.workflow.service.ProcessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <p>
 * 工单信息表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Validated
@RestController
@RequestMapping("/process/process_info")
@Api(value = "工单信息表管理", description = "工单信息表管理")
public class ProcessInfoController extends AbstractController<ProcessInfo, ProcessInfoMapper, ProcessInfoService> {

    @Autowired
    private FlowDefinitionService flowDefinitionService;
    @Autowired
    private ButtonDefService buttonDefService;
    @Autowired
    private OperationRecordService operationRecordService;

    @ApiOperation(value = "预发起工单")
    @RequiresPermissions("process_info_update")
    @GetMapping(value = "/pre_create/{processDefinitionKey}")
    public ResponseEntity<JsonResult<ProcessInfo>> preCreate(
            @ApiParam(value = "流程定义名称", required = true) @PathVariable String processDefinitionKey) {

        ProcessInfo processInfo = service.preCreate(LoginUtil.getCurrentUserAccount(), processDefinitionKey);
        return success(processInfo);
    }

    @ApiOperation(value = "发起工单")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/create/{processDefinitionId}")
    public ResponseEntity<JsonResult> create(
            @ApiParam(value = "流程定义ID", required = true) @PathVariable String processDefinitionId,
            @ApiParam(value = "表单数据（jsonString，{表单字段的key1:表单字段的值1,表单字段的key2:表单字段的值2}）", required = true) @RequestBody @NotBlank String formDatas,
            @ApiParam(value = "是否提交") @RequestParam(required = false, defaultValue = "true") Boolean submit) {

        synchronized (this) {
            service.create(processDefinitionId, LoginUtil.getCurrentUserAccount(), JSONObject.parseObject(formDatas), submit);
        }
        return success();
    }

    @ApiOperation(value = "处理工单")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/handle/{taskId}")
    public ResponseEntity<JsonResult> handle(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "表单数据（jsonString，{表单字段的key1:表单字段的值1,表单字段的key2:表单字段的值2}）", required = true) @RequestBody @NotBlank String formDatas,
            @ApiParam(value = "是否提交") @RequestParam(required = false, defaultValue = "true") Boolean submit) {

        synchronized (this) {
            service.handle(LoginUtil.getCurrentUserAccount(), taskId, JSONObject.parseObject(formDatas), submit);
        }
        return success();
    }

    @ApiOperation(value = "实时提交表单数据")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/submit_formdata/{taskId}")
    public ResponseEntity<JsonResult> submit(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "表单字段键值对", required = true) @RequestBody @NotNull FormDataDTO formDataDTO) {

        synchronized (this) {
            service.submit(LoginUtil.getCurrentUserAccount(), taskId, formDataDTO.getKey(), formDataDTO.getValue());
        }
        return success();
    }

    @ApiOperation(value = "工单回退")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/goback/{processInfoId}")
    public ResponseEntity<JsonResult> goback(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId) {

        synchronized (this) {
            service.goback(processInfoId, LoginUtil.getCurrentUserAccount());
        }
        return success();
    }

    @ApiOperation(value = "工单改派")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/reassign/{processInfoId}")
    public ResponseEntity<JsonResult> reassign(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
            @ApiParam(value = "受让人账号", required = true) @NotBlank(message = "受让人账号为非空") String assignee) {

        synchronized (this) {
            service.reassign(processInfoId, LoginUtil.getCurrentUserAccount(), assignee);
        }
        return success();
    }

    @ApiOperation(value = "删除工单")
    @RequiresPermissions("process_info_update")
    @DeleteMapping(value = "/cancel/{processInfoId}")
    public ResponseEntity<JsonResult> del(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
                                          @ApiParam(value = "删除原因") String reason) {

        synchronized (this) {
            service.delete(LoginUtil.getCurrentUserAccount(), processInfoId, reason);
        }
        return success();
    }

    @ApiOperation(value = "设置工单优先级")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/priority/{processInfoId}")
    public ResponseEntity<JsonResult> priority(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
            @ApiParam(value = "工单优先级", required = true) @NotNull(message = "工单优先级为非空") Integer priority) {

        synchronized (this) {
            service.processPriority(processInfoId, LoginUtil.getCurrentUserAccount(), priority);
        }
        return success();
    }


    @ApiOperation(value = "查询任务列表")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<ProcessInfo>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProcessInfo> page,
            @ApiParam(value = "工单标题") String fuzzyTitle,
            @ApiParam(value = "工单编号") String fuzzyNum,
            @ApiParam(value = "工单优先级") Integer priority,
            @ApiParam(value = "任务状态（TASK_TODO：待办，TASK_DONE：已办，我发起的：I_LAUNCHED）", required = true) @NotNull(message = "任务状态为非空") TaskStatus taskStatus) {

        String currentUser = LoginUtil.getCurrentUserAccount();

        IPage<ProcessInfo> processInfos;
        if (taskStatus.equals(TaskStatus.TASK_TODO)) {
            processInfos = service.queryTasktodoList(currentUser, page, fuzzyTitle, fuzzyNum, priority);
        } else {
            processInfos = service.queryInstanceList(currentUser, page, taskStatus, fuzzyTitle, fuzzyNum, priority);
        }

        return success(processInfos);
    }

    @ApiOperation(value = "查询任务数量")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/count")
    public ResponseEntity<JsonResult<Integer>> count(@ApiParam(value = "任务状态（TASK_TODO：待办）", required = true) @NotNull(message = "任务状态为非空") TaskStatus taskStatus) {

        int num = 0;
        if (taskStatus.equals(TaskStatus.TASK_TODO)) {
            num = service.countTasktodoList(LoginUtil.getCurrentUserAccount());
        }
        return success(num);
    }

    @ApiOperation(value = "查看工单页面")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/view_page/{processInfoId}")
    public ResponseEntity<JsonResult<ProcessInfo>> viewPage(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
            @ApiParam(value = "任务ID，默认为发起任务") String taskDefId) {

        ProcessInfo processInfo = service.getViewPageByTaskDefId(processInfoId, taskDefId);
        return success(processInfo);
    }

    @ApiOperation(value = "处理工单页面")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/handle_page/{taskId}")
    public ResponseEntity<JsonResult<ProcessInfo>> handlePage(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {

        ProcessInfo processInfo = service.getHandlePageByTaskId(LoginUtil.getCurrentUserAccount(), taskId);
        return success(processInfo);
    }

    @ApiOperation(value = "查看流程定义图")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/process_definition_view")
    public ResponseEntity<JsonResult<FlowDefinition>> processDefinitionView(
            @ApiParam(value = "流程定义ID", required = true) @NotBlank String processDefinitionId) {

        FlowDefinition flowDefinition = flowDefinitionService.getFlow(processDefinitionId);
        return success(flowDefinition);
    }

    @ApiOperation(value = "查看工单流转图")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/process_info_view")
    public ResponseEntity<JsonResult<ProcessView>> processInfoView(
            @ApiParam(value = "工单ID", required = true) @NotNull Long processInfoId) {

        ProcessView processView = service.getProcessViewByProcessInfoId(processInfoId);
        return success(processView);
    }

    @ApiOperation(value = "查询按钮")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/button/list")
    public ResponseEntity<JsonResult<List<ButtonDef>>> buttonList(
            @ApiParam(value = "展示页面", required = true) @NotBlank String showPage,
            @ApiParam(value = "流程定义ID", required = true) @NotBlank String processDefinitionId,
            @ApiParam(value = "任务定义ID") String taskDefId,
            @ApiParam(value = "按钮位置（BUTTOM，LEFT_TOP，RIGHT_TOP）") ButtonPosition buttonPosition) {

        List<ButtonDef> buttonDefs = buttonDefService.queryPageButtonList(showPage, processDefinitionId, taskDefId, buttonPosition);
        return success(buttonDefs);
    }

    @ApiOperation(value = "查询工单操作记录表")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/operation_record/{processInfoId}")
    public ResponseEntity<JsonResult<List<OperationRecord>>> operationRecordList(
            @ApiParam(value = "工单信息ID") @PathVariable Long processInfoId) {

        List<OperationRecord> operationRecords = operationRecordService.list(JaguarLambdaQueryWrapper.<OperationRecord>newInstance()
                .eq(OperationRecord::getProcessInfoId, processInfoId));
        return success(operationRecords);
    }
}