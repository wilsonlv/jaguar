package org.jaguar.modules.workflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public List<ButtonDef> queryPageButtonList(String showPage, String processDefinitionKey, String taskDefName,
                                               ButtonPosition buttonPosition) {

        LambdaQueryWrapper<ButtonDef> wrapper = JaguarLambdaQueryWrapper.<ButtonDef>newInstance()
                .eq(ButtonDef::getDefaultSetting, true)
                .like(ButtonDef::getShowPages, showPage)
                .eq(ButtonDef::getButtonPosition, buttonPosition)
                .orderByAsc(ButtonDef::getSortNo);
        List<ButtonDef> buttonDefs = this.list(wrapper);

        Map<String, Object> param = new HashMap<>();
        param.put("processDefinitionKey", processDefinitionKey);
        param.put("showPage", showPage);
        param.put("taskDefName", taskDefName);
        param.put("defaultSetting", true);
        if (buttonPosition != null) {
            param.put("buttonPosition", buttonPosition.toString());
        }
        List<ButtonDef> buttonInsts = mapper.queryButtonInstList(param);
        buttonDefs.addAll(buttonInsts);

        return buttonDefs;
    }

}
