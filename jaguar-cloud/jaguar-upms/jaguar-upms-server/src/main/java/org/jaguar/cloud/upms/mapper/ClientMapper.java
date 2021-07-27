package org.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jaguar.cloud.upms.model.Client;
import org.jaguar.commons.basecrud.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2021-07-27
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {

}