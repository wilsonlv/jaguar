package com.jaguar.process.service;

import com.jaguar.process.mapper.ButtonInstMapper;
import com.jaguar.process.model.po.ButtonDef;
import com.jaguar.process.model.po.ButtonInst;
import com.jaguar.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 按钮实例表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Service
@CacheConfig(cacheNames = "ButtonInst")
public class ButtonInstService extends BaseService<ButtonInst, ButtonInstMapper> {

    @Autowired
    private ButtonDefService buttonDefService;

    /**
     * 获取页面上button
     */
    public List<ButtonDef> queryPageButtonList(Map<String, Object> param) {
        List<ButtonDef> buttonDefs = buttonDefService.query(param).getRecords();

        List<Long> buttonInstIds = mapper.queryPageButtonList(param);
        List<ButtonInst> buttonInsts = getList(buttonInstIds);

        for (ButtonInst buttonInst : buttonInsts) {
            ButtonDef buttonDef = buttonDefService.getById(buttonInst.getButtonDefId());
            buttonDefs.add(buttonDef);
        }
        return buttonDefs;
    }
}
