package top.wilsonlv.jaguar.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Configuration
public class RedisTokenStoreConfig {

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("oauth2:");
        return redisTokenStore;
    }

}
