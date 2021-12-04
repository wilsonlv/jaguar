package top.wilsonlv.jaguar.oauth2.util;

import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import java.net.*;

/**
 * @author lvws
 * @since 2021/10/27
 */
public class MonitorUitl {

    private static final String LOCAL_127 = "127.0.0.1";

    public static String getMonitorIp(String monitorUrl) throws UnknownHostException, SocketException {
        String monitorHost = URI.create(monitorUrl).getHost();
        InetAddress inetAddress = Inet4Address.getByName(monitorHost);
        if (LOCAL_127.equals(inetAddress.getHostAddress())) {
            return WebUtil.getInet4Address().getHostAddress();
        } else {
            return inetAddress.getHostAddress();
        }
    }

    public static String getAccessString(String[] monitorUrls) throws UnknownHostException, SocketException {
        StringBuilder access = new StringBuilder();
        for (int i = 0; i < monitorUrls.length; i++) {
            String url = monitorUrls[i];
            access.append("hasIpAddress('").append(getMonitorIp(url)).append("')");

            if (i + 1 < monitorUrls.length) {
                access.append(" or ");
            }
        }
        return access.toString();
    }

}
