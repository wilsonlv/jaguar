package org.jaguar.modules.codegen.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jaguar.commons.basecrud.BaseMapper;
import org.jaguar.modules.codegen.model.DataSource;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2021-06-16
 */
@Mapper
public interface DataSourceMapper extends BaseMapper<DataSource> {

}