package top.wilsonlv.jaguar.commons.web.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.*;
import java.util.Enumeration;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@Slf4j
public class WebUtil extends WebUtils {

    private static final String UNKNOWN = "unknown";

    private static final String LOCALHOST = "127.0.0.1";

    private static final int IP_LENGTH = 15;

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        } catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return {HttpServletResponse}
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }


    /**
     * 获取客户端IP
     */
    public static String getHost() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }

        return getHost(request);
    }

    /**
     * 获取客户端IP
     */
    public static String getHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (LOCALHOST.equals(ip)) {
            try { // 根据网卡取本机配置的IP
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > IP_LENGTH) {
            int index = ip.indexOf(",");
            if (index > 0) {
                ip = ip.substring(0, index);
            }
        }
        return ip;
    }

    /**
     * 获取主机MAC地址
     *
     * @return 主机MAC地址
     * @throws UnknownHostException e
     * @throws SocketException      e
     */
    public static String getHostMacAddress() throws UnknownHostException, SocketException {
        NetworkInterface netInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        byte[] macAddr = netInterface.getHardwareAddress();

        StringBuilder address = new StringBuilder();
        for (byte b : macAddr) {
            String postion = Integer.toHexString(b & 0xff);
            if (postion.length() == 1) {
                postion = "0" + postion;
            }
            address.append(postion).append('-');
        }
        return address.deleteCharAt(address.length() - 1).toString();
    }

    /*
     * 获取本机网内地址
     */
    public static InetAddress getInet4Address() throws SocketException, UnknownHostException {
        //获取所有网络接口
        Enumeration<NetworkInterface> allNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
        //遍历所有网络接口
        while (allNetworkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = allNetworkInterfaces.nextElement();
            //如果此网络接口为 回环接口 或者 虚拟接口(子接口) 或者 未启用 或者 描述中包含VM
            if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp() || networkInterface.getDisplayName().contains("VM")) {
                //继续下次循环
                continue;
            }

            for (Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses(); inetAddressEnumeration.hasMoreElements(); ) {
                InetAddress inetAddress = inetAddressEnumeration.nextElement();
                //如果此IP不为空
                if (inetAddress instanceof Inet4Address) {
                    //如果此IP为IPV4 则返回
                    return inetAddress;
                }
            }

        }
        throw new UnknownHostException();
    }

}
