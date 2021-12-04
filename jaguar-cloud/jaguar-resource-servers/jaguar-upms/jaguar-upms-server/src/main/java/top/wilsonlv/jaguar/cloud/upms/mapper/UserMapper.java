package top.wilsonlv.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.wilsonlv.jaguar.cloud.upms.entity.User;
import top.wilsonlv.jaguar.basecrud.BaseMapper;

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