package org.jaguar.cloud.upms.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jaguar.cloud.upms.server.model.Login;
import org.jaguar.commons.basecrud.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Mapper
public interface LoginMapper extends BaseMapper<Login> {

}