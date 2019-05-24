package org.jaguar.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.workflow.model.po.FormTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
public interface FormTemplateMapper extends BaseMapper<FormTemplate> {

    List<FormTemplate> queryLatest(IPage page, @Param("name") String name, @Param("fuzzyName") String fuzzyName, @Param("elementId") String elementId);
}