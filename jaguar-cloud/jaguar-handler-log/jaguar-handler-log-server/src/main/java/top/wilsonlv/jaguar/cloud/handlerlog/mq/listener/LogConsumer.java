package top.wilsonlv.jaguar.cloud.handlerlog.mq.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.HandlerLogSaveDTO;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.LoginLogSaveDTO;
import top.wilsonlv.jaguar.cloud.handlerlog.model.HandlerLog;
import top.wilsonlv.jaguar.cloud.handlerlog.model.LoginLog;
import top.wilsonlv.jaguar.cloud.handlerlog.repository.HandlerLogRepository;
import top.wilsonlv.jaguar.cloud.handlerlog.repository.LoginLogRepository;
import top.wilsonlv.jaguar.commons.activemq.ActivemqConstant;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogConsumer {

    private final HandlerLogRepository handlerLogRepository;

    private final LoginLogRepository loginLogRepository;

    @JmsListener(destination = HandlerLogConstant.DESTINATION_HANDLER_LOG, containerFactory = ActivemqConstant.QUEUE_LISTENER)
    public void saveLog(HandlerLogSaveDTO handlerLogSaveDTO) {
        log.debug("接口日志来了:{}", handlerLogSaveDTO.getRequestUri());
        HandlerLog handlerLog = new HandlerLog();
        BeanUtils.copyProperties(handlerLogSaveDTO, handlerLog);
        handlerLogRepository.save(handlerLog);
    }

    @JmsListener(destination = HandlerLogConstant.DESTINATION_LOGIN_LOG, containerFactory = ActivemqConstant.QUEUE_LISTENER)
    public void saveLog(LoginLogSaveDTO loginLogSaveDTO) {
        log.debug("登录日志来了:{}", loginLogSaveDTO.getPrincipal());
        LoginLog loginLog = new LoginLog();
        BeanUtils.copyProperties(loginLogSaveDTO, loginLog);
        loginLogRepository.save(loginLog);
    }

}
