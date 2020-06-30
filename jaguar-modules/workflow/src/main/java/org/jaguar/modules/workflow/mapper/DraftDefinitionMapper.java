package org.jaguar.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.workflow.model.po.DraftDefinition;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
public interface DraftDefinitionMapper extends BaseMapper<DraftDefinition> {

    List<DraftDefinition> queryLatest(IPage<DraftDefinition> page, @Param("p") Map<String, Object> params);
}