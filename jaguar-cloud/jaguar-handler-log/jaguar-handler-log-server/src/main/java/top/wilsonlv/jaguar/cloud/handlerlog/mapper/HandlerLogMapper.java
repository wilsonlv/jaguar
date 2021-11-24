package top.wilsonlv.jaguar.cloud.handlerlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wilsonlv.jaguar.cloud.handlerlog.entity.HandlerLog;

import java.util.List;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/7/15
 */
@Mapper
public interface HandlerLogMapper extends BaseMapper<HandlerLog> {

    List<HandlerLog> queryHandlerLog(@Param("p") Map<String, Object> params);

}
