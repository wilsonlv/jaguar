package top.wilsonlv.jaguar.commons.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/11/18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jaguar.web")
public class JaguarWebProperties implements Serializable {

    private Boolean jsonResultResponseEnable = true;

    private Set<String> jsonResultResponseIgnoreUrls;

}
