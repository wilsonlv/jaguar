package com.jaguar.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaguar.process.model.po.FormTemplate;
import com.jaguar.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
public interface FormTemplateMapper extends BaseMapper<FormTemplate> {

    List<Long> queryLatest(IPage page, @Param("cm") Map<String, Object> params);
}