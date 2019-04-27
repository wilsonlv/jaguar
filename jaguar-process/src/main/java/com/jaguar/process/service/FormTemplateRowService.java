package com.jaguar.process.service;

import com.jaguar.process.model.po.FormTemplateRow;
import com.jaguar.process.mapper.FormTemplateRowMapper;
import com.jaguar.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 表单行表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
@Service
@CacheConfig(cacheNames = "FormTemplateRow")
public class FormTemplateRowService extends BaseService<FormTemplateRow, FormTemplateRowMapper>  {

}
