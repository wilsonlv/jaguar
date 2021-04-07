package org.jaguar.support.handlerlog.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.exception.DataCrudException;
import org.jaguar.support.handlerlog.mapper.HandlerLogMapper;
import org.jaguar.support.handlerlog.model.HandlerLog;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public void saveLog(HandlerLog handlerLog) {
        boolean success = SqlHelper.retBool(handlerLogMapper.insert(handlerLog));
        if (!success) {
            log.error("实体信息：" + handlerLog.toString());
            throw new DataCrudException("数据插入失败！");
        }
    }

}
