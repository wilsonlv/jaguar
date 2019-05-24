package org.jaguar.modules.workflow.service;

import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.workflow.enums.ButtonPosition;
import org.jaguar.modules.workflow.mapper.ButtonDefMapper;
import org.jaguar.modules.workflow.model.po.ButtonDef;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 按钮定义表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Service
public class ButtonDefService extends BaseService<ButtonDef, ButtonDefMapper> {

    /**
     * 获取页面上button
     */
    public List<ButtonDef> queryPageButtonList(String showPage, String processDefinitionId, String taskDefId, ButtonPosition buttonPosition) {

        JaguarLambdaQueryWrapper<ButtonDef> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.eq(ButtonDef::getDefaultSetting, true);
        wrapper.like(ButtonDef::getShowPages, showPage);
        wrapper.eq(ButtonDef::getButtonPosition, buttonPosition);
        wrapper.orderByAsc(ButtonDef::getSortNo);
        List<ButtonDef> buttonDefs = this.list(wrapper);

        Map<String, Object> param = new HashMap<>();
        param.put("processDefinitionId", processDefinitionId);
        param.put("showPage", showPage);
        param.put("taskDefId", taskDefId);
        param.put("defaultSetting", true);
        if (buttonPosition != null) {
            param.put("buttonPosition", buttonPosition.toString());
        }
        List<ButtonDef> buttonInsts = mapper.queryButtonInstList(param);
        buttonDefs.addAll(buttonInsts);

        return buttonDefs;
    }

}
