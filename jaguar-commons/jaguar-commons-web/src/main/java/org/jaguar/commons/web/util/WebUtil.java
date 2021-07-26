package org.jaguar.commons.web.util;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2019/5/23.
 */
@Slf4j
public class WebUtil extends WebUtils {

    private static final String BASIC_ = "Basic ";

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
     * 解析clientId
     *
     * @param header 请求头
     * @return clientId
     */
    public String extractClientId(String header) {
        if (StringUtils.isBlank(header) || !header.startsWith(BASIC_)) {
            log.debug("请求头中client信息为空: {}", header);
            return null;
        }

        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            log.debug("Failed to decode basic authentication token: {}", header);
            return null;
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int colon = token.indexOf(":");
        if (colon == -1) {
            log.debug("Invalid basic authentication token: {}", header);
            return null;
        }
        return token.substring(0, colon);
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
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
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

}