package org.jaguar.modules.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.task.api.Task;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.Page;
import org.jaguar.modules.document.service.DocumentService;
import org.jaguar.modules.workflow.enums.ButtonPosition;
import org.jaguar.modules.workflow.enums.TaskStatus;
import org.jaguar.modules.workflow.mapper.ProcessInfoMapper;
import org.jaguar.modules.workflow.model.dto.FormDataDTO;
import org.jaguar.modules.workflow.model.dto.TaskDTO;
import org.jaguar.modules.workflow.model.po.ButtonDef;
import org.jaguar.modules.workflow.model.po.FormDataAttach;
import org.jaguar.modules.workflow.model.po.OperationRecord;
import org.jaguar.modules.workflow.model.po.ProcessInfo;
import org.jaguar.modules.workflow.model.vo.FlowDefinition;
import org.jaguar.modules.workflow.model.vo.ProcessView;
import org.jaguar.modules.workflow.service.ButtonDefService;
import org.jaguar.modules.workflow.service.FlowDefinitionService;
import org.jaguar.modules.workflow.service.OperationRecordService;
import org.jaguar.modules.workflow.service.ProcessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "预发起工单")
    @RequiresPermissions("process_info_update")
    @GetMapping(value = "/pre_create/{processDefinitionKey}")
    public ResponseEntity<JsonResult<ProcessInfo>> preCreate(@ApiParam(value = "流程定义名称", required = true) @PathVariable String processDefinitionKey) {

        ProcessInfo processInfo = service.preCreate(getCurrentUserAccount(), processDefinitionKey);
        return success(processInfo);
    }

    @ApiOperation(value = "发起工单")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/create/{processDefinitionId}")
    public ResponseEntity<JsonResult<String>> create(
            @ApiParam(value = "流程定义ID", required = true) @PathVariable String processDefinitionId,
            @ApiParam(value = "表单数据（jsonString，{表单字段的key1:表单字段的值1,表单字段的key2:表单字段的值2}）", required = true) @RequestBody @NotBlank String formDatas,
            @ApiParam(value = "是否提交") @RequestParam(required = false, defaultValue = "true") Boolean submit) {

        Task task;
        synchronized (this) {
            task = service.create(processDefinitionId, getCurrentUserAccount(), JSONObject.parseObject(formDatas), submit);
        }
        return success(task != null ? task.getId() : null);
    }

    @ApiOperation(value = "处理工单")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/handle/{taskId}")
    public ResponseEntity<JsonResult> handle(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "表单数据（jsonString，{表单字段的key1:表单字段的值1,表单字段的key2:表单字段的值2}）") @RequestBody(required = false) String formDatas,
            @ApiParam(value = "是否提交") @RequestParam(required = false, defaultValue = "true") Boolean submit,
            @ApiParam(value = "提交驳回") @RequestParam(required = false, defaultValue = "false") Boolean reject,
            @ApiParam(value = "驳回原因") @RequestParam(required = false) String reason) {

        synchronized (this) {
            service.handle(getCurrentUserAccount(), taskId,
                    StringUtils.isNoneBlank(formDatas) ? JSONObject.parseObject(formDatas) : new JSONObject(), submit, reject, reason);
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
            service.submit(getCurrentUserAccount(), taskId, formDataDTO.getKey(), formDataDTO.getValue());
        }
        return success();
    }

    @ApiOperation(value = "文件上传")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/upload/{taskId}")
    public ResponseEntity<JsonResult<List<FormDataAttach>>> upload(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "字段key", required = true) @RequestParam String key,
            @ApiParam(value = "文件", required = true) @RequestParam("files") @NotEmpty List<MultipartFile> files) {

        List<FormDataAttach> formDataAttaches;
        synchronized (this) {
            formDataAttaches = service.upload(getCurrentUserAccount(), taskId, key, files);
        }
        return success(formDataAttaches);
    }

    @ApiOperation(value = "删除附件")
    @RequiresPermissions("process_info_update")
    @DeleteMapping(value = "/delete_attach/{taskId}")
    public ResponseEntity<JsonResult> deleteAttach(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "字段key", required = true) @RequestParam Long formDataAttachId) {

        synchronized (this) {
            service.deleteAttach(getCurrentUserAccount(), taskId, formDataAttachId);
        }
        return success();
    }

    @ApiOperation(value = "任务改派")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/reassign/{taskId}")
    public ResponseEntity<JsonResult> reassign(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "受理人账号", required = true) @RequestParam @NotBlank(message = "受让人账号为非空") String reassignee,
            @ApiParam(value = "备注") @RequestParam(required = false) String remark) {

        synchronized (this) {
            service.reassign(taskId, getCurrentUserAccount(), reassignee, remark);
        }
        return success();
    }

    @ApiOperation(value = "删除我发起的工单")
    @RequiresPermissions("process_info_update")
    @DeleteMapping(value = "/cancel/{processInfoId}")
    public ResponseEntity<JsonResult> delMyOwnProcess(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
                                                      @ApiParam(value = "删除原因") @RequestParam @NotBlank String reason) {

        synchronized (this) {
            service.delete(getCurrentUserAccount(), processInfoId, reason);
        }
        return success();
    }

    @ApiOperation(value = "设置工单优先级")
    @RequiresPermissions("process_info_update")
    @PostMapping(value = "/priority/{processInfoId}")
    public ResponseEntity priority(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
            @ApiParam(value = "工单优先级", required = true) @NotNull(message = "工单优先级为非空") Integer priority) {

        synchronized (this) {
            service.processPriority(processInfoId, getCurrentUserAccount(), priority);
        }
        return success();
    }


    @ApiOperation(value = "查询任务列表")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/page")
    public ResponseEntity<JsonResult<Page<ProcessInfo>>> page(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProcessInfo> page,
            @ApiParam(value = "流程定义名称") @RequestParam(required = false) List<String> processDefinitionName,
            @ApiParam(value = "工单任务名称") @RequestParam(required = false) List<String> taskName,
            @ApiParam(value = "工单标题") @RequestParam(required = false) String fuzzyTitle,
            @ApiParam(value = "工单编号") @RequestParam(required = false) String fuzzyNum,
            @ApiParam(value = "工单优先级") @RequestParam(required = false) Integer priority,
            @ApiParam(value = "任务状态（TASK_TODO：待办，TASK_DONE：已办，我发起的：I_LAUNCHED）", required = true)
            @RequestParam @NotNull(message = "任务状态为非空") TaskStatus taskStatus) {

        String currentUser = getCurrentUserAccount();

        IPage<ProcessInfo> processInfos;
        if (taskStatus.equals(TaskStatus.TASK_TODO)) {
            processInfos = service.queryTasktodoList(currentUser, page, processDefinitionName, taskName, fuzzyTitle, fuzzyNum, priority);
        } else {
            processInfos = service.queryInstanceList(currentUser, page, processDefinitionName, taskStatus, fuzzyTitle, fuzzyNum, priority);
        }
        return success(processInfos);
    }

    @ApiOperation(value = "查询任务数量")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/count")
    public ResponseEntity<JsonResult<Integer>> count(
            @ApiParam(value = "流程定义名称") @RequestParam(required = false) String processDefinitionName,
            @ApiParam(value = "工单任务名称") @RequestParam(required = false) List<String> taskName) {

        int num = service.countTasktodoList(getCurrentUserAccount(), processDefinitionName, taskName);
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

        ProcessInfo processInfo = service.getHandlePageByTaskId(getCurrentUserAccount(), taskId);
        return success(processInfo);
    }

    @ApiOperation(value = "查看流程定义图")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/process_definition_view")
    public ResponseEntity<JsonResult<FlowDefinition>> processDefinitionView(
            @ApiParam(value = "流程定义ID", required = true) @NotEmpty String processDefinitionId) {

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
    public ResponseEntity<JsonResult<List<ButtonDef>>> buttonList(@ApiParam(value = "展示页面", required = true) @NotEmpty String showPage,
                                                                  @ApiParam(value = "流程定义名称", required = true) @NotEmpty String processDefinitionKey,
                                                                  @ApiParam(value = "任务定义名称") String taskDefName,
                                                                  @ApiParam(value = "按钮位置（BUTTOM，LEFT_TOP，RIGHT_TOP）") ButtonPosition buttonPosition) {

        List<ButtonDef> buttonDefs = buttonDefService.queryPageButtonList(showPage, processDefinitionKey, taskDefName, buttonPosition);
        return success(buttonDefs);
    }

    @ApiOperation(value = "查询工单操作记录表")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/operation_record/{processInfoId}")
    public ResponseEntity<JsonResult<List<OperationRecord>>> operationRecordList(@ApiParam(value = "工单信息ID") @PathVariable Long processInfoId) {
        List<OperationRecord> operationRecords = operationRecordService.listByProcessInfoIdWithUser(processInfoId);
        return success(operationRecords);
    }

    @ApiOperation(value = "查询工单回退原因")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/operation_record/go_back_reason/{taskId}")
    public ResponseEntity<JsonResult<OperationRecord>> getTaskGobackReason(@ApiParam(value = "任务实例ID") @PathVariable String taskId) {

        OperationRecord operationRecord = service.getTaskGobackReason(taskId);
        return success(operationRecord);
    }

    @ApiOperation(value = "查看已办任务信息")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/done_task_info/{processInfoId}")
    public ResponseEntity<JsonResult<TaskDTO>> getDoneTaskInfo(@ApiParam(value = "工单ID") @PathVariable Long processInfoId,
                                                               @ApiParam(value = "工单任务名称") @RequestParam @NotBlank String taskName) {

        TaskDTO taskInfo = service.getDoneTaskInfo(processInfoId, taskName);
        return success(taskInfo);
    }

    @ApiOperation(value = "检验字段唯一性")
    @RequiresPermissions("process_info_view")
    @GetMapping(value = "/form_data/exist")
    public ResponseEntity<JsonResult<Boolean>> formDataExist(
            @ApiParam(value = "流程定义名称", required = true) @RequestParam @NotBlank String processDefinitionName,
            @ApiParam(value = "字段key", required = true) @RequestParam @NotBlank String key,
            @ApiParam(value = "字段value", required = true) @RequestParam @NotBlank String value,
            @ApiParam(value = "工单ID") @RequestParam(required = false) Long processInfoId) {

        boolean unique = service.checkUnique(processDefinitionName, key, value, processInfoId);
        return success(unique);
    }


    @ApiOperation(value = "查询工单列表")
    @RequiresPermissions("process_info_mgm")
    @GetMapping(value = "/mgm/page")
    public ResponseEntity<JsonResult<Page<ProcessInfo>>> mgmPage(
            @ApiParam(value = "分页信息") com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProcessInfo> page,
            @ApiParam(value = "流程定义名称") @RequestParam(required = false) List<String> processDefinitionName,
            @ApiParam(value = "工单标题") @RequestParam(required = false) String fuzzyTitle,
            @ApiParam(value = "工单编号") @RequestParam(required = false) String fuzzyNum,
            @ApiParam(value = "工单优先级") @RequestParam(required = false) Integer priority) {

        IPage<ProcessInfo> processInfos = service.queryProcessInfo(page, processDefinitionName, fuzzyTitle, fuzzyNum, priority);
        return success(processInfos);
    }

    @ApiOperation(value = "删除工单")
    @RequiresPermissions("process_info_mgm")
    @DeleteMapping(value = "/mgm/cancel/{processInfoId}")
    public ResponseEntity<JsonResult> del(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
                                          @ApiParam(value = "删除原因") @RequestParam @NotBlank String reason) {

        synchronized (this) {
            service.delete(getCurrentUserAccount(), processInfoId, reason);
        }
        return success();
    }

    @ApiOperation(value = "挂起工单")
    @RequiresPermissions("process_info_mgm")
    @PostMapping(value = "/mgm/suspend/{processInfoId}")
    public ResponseEntity<JsonResult> suspend(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId) {

        synchronized (this) {
            service.suspend(getCurrentUserAccount(), processInfoId);
        }
        return success();
    }

    @ApiOperation(value = "激活工单")
    @RequiresPermissions("process_info_mgm")
    @PostMapping(value = "/mgm/activate/{processInfoId}")
    public ResponseEntity<JsonResult> activate(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId) {

        synchronized (this) {
            service.activate(getCurrentUserAccount(), processInfoId);
        }
        return success();
    }


    /**
     * flowable异常处理
     */
    @ResponseBody
    @ExceptionHandler(value = FlowableException.class)
    public ResponseEntity flowableExceptionHandler(Exception exception) {
        exception.printStackTrace();
        Throwable rootCause = exception;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new JsonResult<String>().setMessage(rootCause.getMessage()));
    }
}