package org.jaguar.modules.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.jaguar.core.base.BaseMapper;
import org.jaguar.modules.workflow.model.po.ButtonDef;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
public interface ButtonDefMapper extends BaseMapper<ButtonDef> {

    List<ButtonDef> queryButtonInstList(@Param("p") Map<String, Object> param);

}