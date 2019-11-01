package org.jaguar.modules.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.workflow.model.po.FormData;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
public interface FormDataMapper extends BaseMapper<FormData> {

    FormData getByKeyAndValueInProcessDefinition(@Param("processDefinitionName") String processDefinitionName,
                                                 @Param("key") String key, @Param("value") String value);

}