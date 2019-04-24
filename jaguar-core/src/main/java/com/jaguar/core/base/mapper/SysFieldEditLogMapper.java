package com.jaguar.core.base.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaguar.core.base.model.SysFieldEditLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
public interface SysFieldEditLogMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<SysFieldEditLog> {

    List<SysFieldEditLog> selectEntityPage(IPage page, @Param("cm") Map<String, Object> param);

}