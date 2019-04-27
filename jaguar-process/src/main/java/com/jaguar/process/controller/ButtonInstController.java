package com.jaguar.process.controller;

import com.jaguar.core.enums.OrderType;
import com.jaguar.core.util.InstanceUtil;
import com.jaguar.process.enums.ButtonPosition;
import com.jaguar.process.model.po.ButtonDef;
import com.jaguar.process.model.po.ButtonInst;
import com.jaguar.process.service.ButtonInstService;
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
 * <p>
 * 按钮实例表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@RestController
@Api(value = "按钮实例表管理", description = "按钮实例表管理")
@RequestMapping("/button_inst")
public class ButtonInstController extends AbstractController<ButtonInstService> {

    @ApiOperation(value = "查询按钮实例表")
    @RequiresPermissions("buttonInst:read")
    @GetMapping(value = "/list")
    public Object query(
            @ApiParam(value = "页码") @RequestParam(required = false) Integer page,
            @ApiParam(value = "页量") @RequestParam(required = false) Integer rows) {

        Map<String, Object> param = InstanceUtil.newHashMap();
        param.put(PAGE, page);
        param.put(ROWS, rows);
        param.put(DELETED, 0);

        return super.query(param);
    }

    @ApiOperation(value = "按钮实例表详情")
    @RequiresPermissions("buttonInst:read")
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<JsonResult> get(@PathVariable Long id) {
        return super.get(id);
    }

    @ApiOperation(value = "修改按钮实例表")
    @RequiresPermissions("buttonInst:update")
    @PostMapping(value = "/update")
    public Object update(ButtonInst param) {
        return super.update(param);
    }

    @ApiOperation(value = "删除按钮实例表")
    @RequiresPermissions("buttonInst:update")
    @DeleteMapping(value = "/del/{id}")
    public ResponseEntity<JsonResult> del(@PathVariable Long id) {
        return super.del(id);
    }

    @ApiOperation(value = "删除按钮实例表")
    @RequiresPermissions("buttonInst:update")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<JsonResult> delete(@PathVariable Long id) {
        return super.delete(id);
    }


    @ApiOperation(value = "查询按钮")
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
        param.put(DELETED, 0);

        param.put("showPage", showPage);
        param.put("defaultSetting", true);
        param.put("processDefinitionId", processDefinitionId);
        param.put("taskDefId", taskDefId);
        if (buttonPosition != null) {
            param.put("buttonPosition", buttonPosition.toString());
        }

        List<ButtonDef> buttonDefs = service.queryPageButtonList(param);
        return setSuccessJsonResult(buttonDefs);
    }
}