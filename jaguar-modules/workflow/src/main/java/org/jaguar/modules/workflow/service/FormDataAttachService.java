package org.jaguar.modules.workflow.service;

import org.jaguar.core.base.BaseService;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.modules.workflow.mapper.FormDataAttachMapper;
import org.jaguar.modules.workflow.model.po.FormDataAttach;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 表单数据附件表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-27
 */
@Service
public class FormDataAttachService extends BaseService<FormDataAttach, FormDataAttachMapper> {

    public FormDataAttach getByFormDataId(Long formDataId) {
        return this.unique(JaguarLambdaQueryWrapper.<FormDataAttach>newInstance()
                .eq(FormDataAttach::getFormDataId, formDataId));
    }

}
