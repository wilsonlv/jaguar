package top.wilsonlv.jaguar.cloud.handlerlog.mq.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.HandlerLogSaveDTO;
import top.wilsonlv.jaguar.cloud.handlerlog.model.HandlerLog;
import top.wilsonlv.jaguar.cloud.handlerlog.repository.HandlerLogRepository;
import top.wilsonlv.jaguar.cloud.handlerlog.service.HandlerLogService;
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

    @JmsListener(destination = HandlerLogConstant.DESTINATION_HANDLER_LOG, containerFactory = ActivemqConstant.QUEUE_LISTENER)
    public void saveLog(HandlerLogSaveDTO handlerLogSaveDTO) {
        log.debug("日志来了:{}", handlerLogSaveDTO.getRequestUri());
        HandlerLog handlerLog = new HandlerLog();
        BeanUtils.copyProperties(handlerLogSaveDTO, handlerLog);
        handlerLogRepository.save(handlerLog);
    }

}
