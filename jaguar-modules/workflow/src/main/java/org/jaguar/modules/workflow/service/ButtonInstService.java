package org.jaguar.modules.workflow.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jaguar.core.base.BaseService;
import org.jaguar.modules.workflow.mapper.ButtonInstMapper;
import org.jaguar.modules.workflow.model.po.ButtonDef;
import org.jaguar.modules.workflow.model.po.ButtonInst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 按钮实例表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Service
public class ButtonInstService extends BaseService<ButtonInst, ButtonInstMapper> {

    @Autowired
    private ButtonDefService buttonDefService;

    @Transactional
    public Page<ButtonInst> queryWithButtonDef(Page<ButtonInst> page, LambdaQueryWrapper<ButtonInst> wrapper) {
        page = this.query(page, wrapper);

        for (ButtonInst buttonInst : page.getRecords()) {
            ButtonDef buttonDef = buttonDefService.getById(buttonInst.getButtonDefId());
            buttonInst.setButtonDef(buttonDef);
        }
        return page;
    }

}
