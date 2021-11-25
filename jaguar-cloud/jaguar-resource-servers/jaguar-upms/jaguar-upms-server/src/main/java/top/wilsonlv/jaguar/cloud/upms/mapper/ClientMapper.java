package top.wilsonlv.jaguar.cloud.upms.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.commons.basecrud.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author lvws
 * @since 2021-07-27
 */
@Mapper
public interface ClientMapper extends BaseMapper<OauthClient> {

}