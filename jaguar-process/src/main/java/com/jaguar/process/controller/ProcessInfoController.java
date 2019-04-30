package com.jaguar.process.controller;

import com.alibaba.fastjson.JSONObject;
import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.Assert;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.enums.ButtonPosition;
import com.jaguar.process.enums.TaskStatus;
import com.jaguar.process.model.po.ButtonDef;
import com.jaguar.process.model.po.ProcessInfo;
import com.jaguar.process.model.vo.FlowDefinition;
import com.jaguar.process.model.vo.ProcessView;
import com.jaguar.process.service.ButtonInstService;
import com.jaguar.process.service.FlowDefinitionService;
import com.jaguar.process.service.ProcessInfoService;
import com.jaguar.web.JsonResult;
import com.jaguar.web.base.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.jaguar.core.constant.Constant.*;

/**
 * <p>
 * 工单信息表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@RestController
@Api(value = "工单信息表管理", description = "工单信息表管理")
@RequestMapping("/process_info")
public class ProcessInfoController extends AbstractController<ProcessInfoService> {

    @Autowired
    private FlowDefinitionService flowDefinitionService;
    @Autowired
    private ButtonInstService buttonInstService;

    @ApiOperation(value = "预发起工单")
    @RequiresPermissions("processInfo:update")
    @GetMapping(value = "/pre_create/{processDefinitionKey}")
    public Object preCreate(@ApiParam(value = "流程定义名称", required = true) @PathVariable String processDefinitionKey) {

        ProcessInfo processInfo = service.preCreate(getCurrentUserAccount(), processDefinitionKey);
        return setSuccessJsonResult(processInfo);
    }

    @ApiOperation(value = "发起工单")
    @RequiresPermissions("processInfo:update")
    @PostMapping(value = "/create/{processDefinitionId}")
    public Object create(
            @ApiParam(value = "流程定义ID", required = true) @PathVariable String processDefinitionId,
            @ApiParam(value = "表单数据（jsonString，{表单字段的key1:表单字段的值1,表单字段的key2:表单字段的值2}）",
                    required = true) @RequestParam String formDatas,
            @ApiParam(value = "是否提交") @RequestParam(required = false, defaultValue = "true") Boolean submit) {

        Assert.notNull(processDefinitionId, "流程定义ID");

        synchronized (this) {
            service.create(processDefinitionId, getCurrentUserAccount(), JSONObject.parseObject(formDatas), submit);
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "处理工单")
    @RequiresPermissions("processInfo:update")
    @PostMapping(value = "/handle/{taskId}")
    public Object handle(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "表单数据（jsonString，{表单字段的key1:表单字段的值1,表单字段的key2:表单字段的值2}）",
                    required = true) @RequestParam String formDatas,
            @ApiParam(value = "是否提交") @RequestParam(required = false, defaultValue = "true") Boolean submit) {
        Assert.notNull(taskId, "任务ID");

        synchronized (this) {
            service.handle(getCurrentUserAccount(), taskId, JSONObject.parseObject(formDatas), submit);
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "实时提交表单数据")
    @RequiresPermissions("processInfo:update")
    @PostMapping(value = "/submit_formdata/{taskId}")
    public Object submit(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @ApiParam(value = "表单字段key", required = true) @RequestParam String key,
            @ApiParam(value = "表单字段value", required = true) @RequestParam String value) {
        Assert.notNull(taskId, "任务ID");

        synchronized (this) {
            service.submit(getCurrentUserAccount(), taskId, key, value);
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "工单回退")
    @RequiresPermissions("processInfo:update")
    @PostMapping(value = "/goback/{processInfoId}")
    public Object goback(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId) {

        synchronized (this) {
            service.goback(processInfoId, getCurrentUserAccount());
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "工单改派")
    @RequiresPermissions("processInfo:update")
    @PostMapping(value = "/reassign/{processInfoId}")
    public Object reassign(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
            @ApiParam(value = "受让人账号", required = true) @PathVariable String assignee) {

        synchronized (this) {
            service.reassign(processInfoId, getCurrentUserAccount(), assignee);
        }
        return setSuccessJsonResult();
    }

    @ApiOperation(value = "删除工单")
    @RequiresPermissions("processInfo:update")
    @DeleteMapping(value = "/cancel/{processInfoId}")
    public Object del(@ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
                      @ApiParam(value = "删除原因") @RequestParam(required = false) String reason) {

        synchronized (this) {
            service.delete(getCurrentUserAccount(), processInfoId, reason);
        }
        return setSuccessJsonResult();
    }


    @ApiOperation(value = "查询任务列表")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/list")
    public ResponseEntity<JsonResult> query(
            @ApiParam(value = "首条记录位置", required = true) @RequestParam Integer first,
            @ApiParam(value = "查询数量", required = true) @RequestParam Integer offset,
            @ApiParam(value = "任务状态（TASK_TODO：待办，TASK_DONE：已办，我发起的：I_LAUNCHED）", required = true)
            @RequestParam TaskStatus taskStatus) {

        String currentUser = getCurrentUserAccount();

        List<ProcessInfo> processInfos = null;
        if (taskStatus.equals(TaskStatus.TASK_TODO)) {
            processInfos = service.queryTasktodoList(currentUser, first, offset);
        } else if (taskStatus.equals(TaskStatus.TASK_DONE)) {
            processInfos = service.queryTaskdoneList(currentUser, first, offset);
        } else if (taskStatus.equals(TaskStatus.I_LAUNCHED)) {
            processInfos = service.queryIlaunchedList(currentUser, first, offset);
        }

        return setSuccessJsonResult(processInfos);
    }

    @ApiOperation(value = "查询任务数量")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/count")
    public ResponseEntity<JsonResult> count(
            @ApiParam(value = "任务状态（TASK_TODO：待办）", required = true) @RequestParam TaskStatus taskStatus) {

        long num = 0;
        if (taskStatus.equals(TaskStatus.TASK_TODO)) {
            num = service.countTasktodoList(getCurrentUserAccount());
        }
        return setSuccessJsonResult(num);
    }

    @ApiOperation(value = "查看工单页面")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/view_page/{processInfoId}")
    public ResponseEntity<JsonResult> viewPage(
            @ApiParam(value = "工单信息ID", required = true) @PathVariable Long processInfoId,
            @ApiParam(value = "任务ID，默认为发起任务") @RequestParam(required = false) String taskId) {

        ProcessInfo processInfo = service.getViewPageByTaskId(processInfoId, taskId);
        return setSuccessJsonResult(processInfo);
    }

    @ApiOperation(value = "处理工单页面")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/handle_page/{taskId}")
    public ResponseEntity<JsonResult> handlePage(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {

        ProcessInfo processInfo = service.getHandlePageByTaskId(getCurrentUserAccount(), taskId);
        return setSuccessJsonResult(processInfo);
    }

    @ApiOperation(value = "查看流程定义图")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/process_definition_view")
    public ResponseEntity<JsonResult> processDefinitionView(
            @ApiParam(value = "流程定义ID", required = true) @RequestParam String processDefinitionId) {

        FlowDefinition flowDefinition = flowDefinitionService.getFlow(processDefinitionId);
        return setSuccessJsonResult(flowDefinition);
    }

    @ApiOperation(value = "查看工单流转图")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/process_info_view")
    public ResponseEntity<JsonResult> processInfoView(
            @ApiParam(value = "工单ID", required = true) @RequestParam Long processInfoId) {

        ProcessView processView = service.getProcessViewByProcessInfoId(processInfoId);
        return setSuccessJsonResult(processView);
    }

    @ApiOperation(value = "查询按钮")
    @RequiresPermissions("processInfo:read")
    @GetMapping(value = "/button/list")
    public ResponseEntity<JsonResult> buttonList(@ApiParam(value = "展示页面", required = true) @RequestParam String showPage,
                                                 @ApiParam(value = "流程定义ID", required = true) @RequestParam String processDefinitionId,
                                                 @ApiParam(value = "任务定义ID") @RequestParam(required = false) String taskDefId,
                                                 @ApiParam(value = "按钮位置（BUTTOM，LEFT_TOP，RIGHT_TOP）") @RequestParam(required = false) ButtonPosition buttonPosition) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, 1);
        param.put(ROWS, 100);
        param.put(SORT, "sort_no");
        param.put(ORDER, OrderType.ASC);

        param.put("showPage", showPage);
        param.put("defaultSetting", true);
        param.put("processDefinitionId", processDefinitionId);
        param.put("taskDefId", taskDefId);
        if (buttonPosition != null) {
            param.put("buttonPosition", buttonPosition.toString());
        }

        List<ButtonDef> buttonDefs = buttonInstService.queryPageButtonList(param);
        return setSuccessJsonResult(buttonDefs);
    }

}