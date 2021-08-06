package top.wilsonlv.jaguar.cloud.handlerlog.mq.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.model.HandlerLog;
import top.wilsonlv.jaguar.cloud.handlerlog.service.HandlerLogService;
import top.wilsonlv.jaguar.commons.activemq.ActivemqConstant;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Component
@RequiredArgsConstructor
public class LogConsumer {

    private final HandlerLogService handlerLogService;

    @JmsListener(destination = HandlerLogConstant.DESTINATION_HANDLER_LOG, containerFactory = ActivemqConstant.QUEUE_LISTENER)
    public void saveLog(HandlerLog handlerLog) {
        handlerLogService.insert(handlerLog);
    }

}
