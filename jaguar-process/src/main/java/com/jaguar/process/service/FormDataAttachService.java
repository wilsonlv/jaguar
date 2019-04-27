package com.jaguar.process.service;

import com.jaguar.process.mapper.FormDataAttachMapper;
import com.jaguar.process.model.po.FormDataAttach;
import com.jaguar.core.base.BaseService;
import com.jaguar.core.util.InstanceUtil;
import org.springframework.cache.annotation.CacheConfig;
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
@CacheConfig(cacheNames = "FormDataAttach")
public class FormDataAttachService extends BaseService<FormDataAttach, FormDataAttachMapper> {

    public FormDataAttach getByFormDataId(Long formDataId) {
        return this.unique(InstanceUtil.newHashMap("formDataId", formDataId));
    }

}
