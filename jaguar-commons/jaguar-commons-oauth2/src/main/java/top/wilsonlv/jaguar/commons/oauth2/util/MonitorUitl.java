package top.wilsonlv.jaguar.commons.oauth2.util;

import org.apache.commons.lang3.ArrayUtils;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import java.net.*;

/**
 * @author lvws
 * @since 2021/10/27
 */
public class MonitorUitl {

    private static final String LOCAL_127 = "127.0.0.1";

    public static String getMonitorIp(String[] monitorUrls) throws UnknownHostException, SocketException {
        String monitorHost;
        if (ArrayUtils.isEmpty(monitorUrls)) {
            monitorHost = "jaguar-monitor";
        } else {
            monitorHost = URI.create(monitorUrls[0]).getHost();
        }

        InetAddress inetAddress = Inet4Address.getByName(monitorHost);
        if (LOCAL_127.equals(inetAddress.getHostAddress())) {
            return WebUtil.getInet4Address().getHostAddress();
        } else {
            return inetAddress.getHostAddress();
        }
    }

}
