package com.jaguar.sys.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jaguar.sys.log.model.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author lvws
 * @since 2018-11-26
 */
public interface SysLogMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<SysLog> {

    List<SysLog> selectPage(IPage page, @Param("cm") Map<String, Object> param);

}