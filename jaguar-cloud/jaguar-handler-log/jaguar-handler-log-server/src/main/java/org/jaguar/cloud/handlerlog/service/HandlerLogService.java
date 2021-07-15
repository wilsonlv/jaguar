package org.jaguar.cloud.handlerlog.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.cloud.handlerlog.mapper.HandlerLogMapper;
import org.jaguar.cloud.handlerlog.model.HandlerLog;
import org.jaguar.commons.web.exception.impl.DataCrudException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2018-11-26
 */
@Slf4j
@Service
public class HandlerLogService {

    @Autowired
    private HandlerLogMapper handlerLogMapper;

    @Async
    @Transactional
    public void saveLog(HandlerLog handlerLog) {
        boolean success = SqlHelper.retBool(handlerLogMapper.insert(handlerLog));
        if (!success) {
            log.error("实体信息：" + handlerLog.toString());
            throw new DataCrudException("数据插入失败！");
        }
    }

}
