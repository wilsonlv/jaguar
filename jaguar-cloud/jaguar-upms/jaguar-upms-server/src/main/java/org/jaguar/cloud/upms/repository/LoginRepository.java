package org.jaguar.cloud.upms.repository;

import org.jaguar.cloud.upms.model.Login;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lvws
 * @since 2021/7/26
 */
@Repository
public interface LoginRepository extends ElasticsearchRepository<Login, Long> {

}
