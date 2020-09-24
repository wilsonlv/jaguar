package org.jaguar.support.redis.cache;

/**
 * @author lvws
 * @since 2020/9/16
 */
public enum EntityCacheableStrategy {

    /**
     * 查询：直接缓存
     * 新增和更新：事务提交后缓存
     * 删除时：删除缓存
     */
    ALWAYS,
    /**
     * 查询：如果没有开启事务，则缓存
     * 新增和更新：事务提交后删除
     * 删除时：删除缓存
     */
    NON_TRANSACTIONAL


}
