package top.wilsonlv.jaguar.commons.oauth2.util;

import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author lvws
 * @since 2021/10/27
 */
public class MonitorUitl {

    private static final String LOCAL_127 = "127.0.0.1";

    public static String getMonitorIp() throws UnknownHostException, SocketException {
        InetAddress inetAddress = Inet4Address.getByName("jaguar-monitor");
        if (LOCAL_127.equals(inetAddress.getHostAddress())) {
            return WebUtil.getInet4Address().getHostAddress();
        } else {
            return inetAddress.getHostAddress();
        }
    }

}
