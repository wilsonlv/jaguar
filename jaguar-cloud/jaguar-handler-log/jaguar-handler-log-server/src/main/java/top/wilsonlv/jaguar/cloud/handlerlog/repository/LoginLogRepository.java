package top.wilsonlv.jaguar.cloud.handlerlog.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.wilsonlv.jaguar.cloud.handlerlog.model.LoginLog;

/**
 * @author lvws
 * @since 2021/8/6
 */
@Repository
public interface LoginLogRepository extends ElasticsearchRepository<LoginLog, Long> {
}
