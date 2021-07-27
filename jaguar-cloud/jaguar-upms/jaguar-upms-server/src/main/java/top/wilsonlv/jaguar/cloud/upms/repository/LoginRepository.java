package top.wilsonlv.jaguar.cloud.upms.repository;

import top.wilsonlv.jaguar.cloud.upms.model.Login;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Repository
public interface LoginRepository extends ElasticsearchRepository<Login, Long> {

}
