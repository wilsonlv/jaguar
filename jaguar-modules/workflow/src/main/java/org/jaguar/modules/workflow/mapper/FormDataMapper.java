package org.jaguar.modules.workflow.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.workflow.model.po.FormData;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-02-28
 */
public interface FormDataMapper extends BaseMapper<FormData> {

    /**
     * 在指定的流程定义中，根据key和value查询
     *
     * @param processDefinitionName 指定的流程定义
     * @param key                   key
     * @param value                 value
     * @return 表单数据实体
     */
    FormData getByKeyAndValueInProcessDefinition(@Param("processDefinitionName") String processDefinitionName,
                                                 @Param("key") String key, @Param("value") String value);

    /**
     * 查询保存方式为重写的数据
     *
     * @param page            分页
     * @param processInfoId   工单ID
     * @param fuzzyFieldLabel 模糊字段标签
     * @param fuzzyFieldKey   模糊字段key
     * @return 分页的表单字段实体
     */
    Page<FormData> queryOverride(Page<FormData> page, @Param("processInfoId") @NotNull Long processInfoId,
                                 @Param("fuzzyFieldLabel") String fuzzyFieldLabel, @Param("fuzzyFieldKey") String fuzzyFieldKey);

}