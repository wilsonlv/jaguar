package top.wilsonlv.jaguar.cloud.handlerlog.service;

import org.springframework.stereotype.Service;
import top.wilsonlv.jaguar.cloud.handlerlog.mapper.LoginLogMapper;
import top.wilsonlv.jaguar.cloud.handlerlog.entity.LoginLog;
import top.wilsonlv.jaguar.commons.basecrud.BaseService;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Service
public class LoginLogService extends BaseService<LoginLog, LoginLogMapper> {
}
