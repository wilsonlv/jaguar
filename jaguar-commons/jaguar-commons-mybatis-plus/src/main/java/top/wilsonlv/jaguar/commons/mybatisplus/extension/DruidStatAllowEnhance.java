package top.wilsonlv.jaguar.commons.mybatisplus.extension;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidStatViewServletConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author lvws
 * @since 2021/11/19
 */
@Configuration
@AutoConfigureBefore(DruidStatViewServletConfiguration.class)
public class DruidStatAllowEnhance {

    public DruidStatAllowEnhance(DruidStatProperties druidStatProperties) throws UnknownHostException {
        DruidStatProperties.StatViewServlet statViewServlet = druidStatProperties.getStatViewServlet();
        String allow = statViewServlet.getAllow();
        if (StringUtils.hasText(allow)) {
            InetAddress inetAddress = Inet4Address.getByName(allow);
            statViewServlet.setAllow(inetAddress.getHostAddress());
        }
    }

}
