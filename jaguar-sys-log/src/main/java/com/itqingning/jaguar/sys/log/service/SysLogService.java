package com.itqingning.jaguar.sys.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itqingning.core.base.BaseService;
import com.itqingning.jaguar.sys.log.mapper.SysLogMapper;
import com.itqingning.jaguar.sys.log.model.SysLog;
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

    public Page query(Map<String, Object> param) {
        Page page = BaseService.getPage(param);
        List<SysLog> sysLogs = sysLogMapper.selectPage(page, param);
        return page.setRecords(sysLogs);
    }

}
