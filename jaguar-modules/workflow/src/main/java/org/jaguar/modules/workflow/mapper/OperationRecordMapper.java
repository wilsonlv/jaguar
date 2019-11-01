package org.jaguar.modules.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.workflow.model.po.OperationRecord;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
public interface OperationRecordMapper extends BaseMapper<OperationRecord> {

    List<OperationRecord> listGroupByBatchNum(@Param("processInfoId") Long processInfoId);

}