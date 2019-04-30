package com.jaguar.sys.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaguar.core.base.BaseService;
import com.jaguar.sys.log.mapper.SysLogMapper;
import com.jaguar.sys.log.model.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2018-11-26
 */
@Service
@CacheConfig(cacheNames = "SysLog")
public class SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Transactional
    public void saveLog(SysLog sysRequestLog) {
        sysRequestLog.setCreateTime(new Date());
        sysLogMapper.insert(sysRequestLog);
    }

    public Page<SysLog> query(Map<String, Object> param) {
        Page<SysLog> page = BaseService.getPage(param, SysLog.class);
        List<SysLog> sysLogs = sysLogMapper.selectEntityPage(page, param);
        return page.setRecords(sysLogs);
    }

}
