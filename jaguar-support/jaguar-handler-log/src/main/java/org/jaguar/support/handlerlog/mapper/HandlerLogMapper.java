package org.jaguar.support.handlerlog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jaguar.support.handlerlog.model.HandlerLog;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author lvws
 * @since 2018-11-26
 */
@Mapper
public interface HandlerLogMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<HandlerLog> {

}