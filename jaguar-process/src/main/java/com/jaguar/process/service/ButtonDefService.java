package com.jaguar.process.service;

import com.jaguar.core.base.BaseService;
import com.jaguar.process.model.po.ButtonDef;
import com.jaguar.process.mapper.ButtonDefMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 按钮定义表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-04
 */
@Service
@CacheConfig(cacheNames = "ButtonDef")
public class ButtonDefService extends BaseService<ButtonDef, ButtonDefMapper> {

}
