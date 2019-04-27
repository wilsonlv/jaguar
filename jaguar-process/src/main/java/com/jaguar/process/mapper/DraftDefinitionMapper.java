package com.jaguar.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaguar.process.model.po.DraftDefinition;
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
 * @since 2019-03-06
 */
public interface DraftDefinitionMapper extends BaseMapper<DraftDefinition> {

    List<Long> queryLatest(IPage page, @Param("cm") Map<String, Object> params);
}