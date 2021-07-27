package top.wilsonlv.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.wilsonlv.jaguar.cloud.upms.model.User;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2019-11-08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}