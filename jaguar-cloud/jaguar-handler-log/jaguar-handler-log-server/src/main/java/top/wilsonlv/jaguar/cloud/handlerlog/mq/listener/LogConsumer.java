package top.wilsonlv.jaguar.cloud.handlerlog.mq.listener;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.HandlerLogSaveDTO;
import top.wilsonlv.jaguar.cloud.handlerlog.entity.HandlerLog;
import top.wilsonlv.jaguar.cloud.handlerlog.repository.HandlerLogRepository;
import top.wilsonlv.jaguar.commons.activemq.ActivemqConstant;

import java.time.ZoneId;
import java.util.Date;

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
        log.debug("接口日志来了:{}", handlerLogSaveDTO.getRequestUri());
        HandlerLog handlerLog = handlerLogSaveDTO.toEntity(HandlerLog.class);
        handlerLog.setId(IdWorker.getId());
        handlerLog.setAccessTime(Date.from(handlerLogSaveDTO.getAccessTime().atZone(ZoneId.systemDefault()).toInstant()));
        handlerLogRepository.save(handlerLog);
    }

}
