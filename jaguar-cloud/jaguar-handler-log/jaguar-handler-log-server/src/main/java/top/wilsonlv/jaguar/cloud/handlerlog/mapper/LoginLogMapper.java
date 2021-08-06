package top.wilsonlv.jaguar.cloud.handlerlog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.wilsonlv.jaguar.cloud.handlerlog.model.LoginLog;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}
