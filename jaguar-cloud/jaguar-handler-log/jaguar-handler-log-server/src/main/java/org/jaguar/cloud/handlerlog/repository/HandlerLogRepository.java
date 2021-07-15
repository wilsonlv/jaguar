package org.jaguar.support.handlerlog.repository;

import org.jaguar.support.handlerlog.model.HandlerLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lvws
 * @since 2021/7/15
 */
@Repository
public interface HandlerLogRepository extends ElasticsearchRepository<HandlerLog, Long> {

}
