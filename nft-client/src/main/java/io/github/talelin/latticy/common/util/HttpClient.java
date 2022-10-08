/**
 * @作者 YuKok
 * @创建时间 2020/5/13 16:36
 */
package io.github.talelin.latticy.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Component
public class HttpClient {

    static RestTemplate restTemplate;
    static ObjectMapper mapper = new ObjectMapper();
    private static HttpServletRequest request;

    public static HttpServletRequest getRequest() {
        return request;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        HttpClient.request = request;
    }

    public static String getRequestUri() {
        return HttpClient.request.getRequestURI();
    }

    public static String getRequestParms() {
        return HttpClient.request.getQueryString();
    }

    public static String getRequestUrl() {
        return HttpClient.request.getRequestURI();
    }

//    public static <T> T exchange(String url,
//                                 HttpMethod method,
//                                 Map<String, Object> body,
//                                 Map<String, Object> params,
//                                 Class<T> clazzz) {
//        HttpEntity<?> entity = null;
//        if (Objects.nonNull(body)) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            String value = "{}";
//            try {
//                value = mapper.writeValueAsString(body);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            entity = new HttpEntity<String>(value,params, headers);
//        } else {
//            entity = new HttpEntity<>(params);
//        }
//        return restTemplate.exchange(url, method, entity, clazzz, params).getBody();
//    }

    public static ResponseEntity<String> get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public static ResponseEntity<String> post(String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }


    public static Cookie[] getCookies(HttpServletRequest request) {
        return request.getCookies();
    }

    public static Boolean hasCookies(HttpServletRequest request) {
        return !(request.getCookies().length == 0);
    }

    public static String getCookie(String key, HttpServletRequest request) {
        if (!HttpClient.hasCookies(request)) {
            return null;
        }
        if (HttpClient.hasCookie(key, request)) {
            for (int i = 0; i < HttpClient.getCookies(request).length; i++) {
                if (HttpClient.getCookies(request)[i].getName().equals(key)) {
                    return HttpClient.getCookies(request)[i].getValue();
                }
            }
        }
        return null;
    }

    public static Boolean hasCookie(String key, HttpServletRequest request) {
        if (!HttpClient.hasCookies(request)) {
            return false;
        }
        for (Cookie cookie : HttpClient.getCookies(request)) {
            if (cookie.getName().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static String getHeader(String key) {
        return request.getHeader(key);
    }

    public static String getCookieAtFirst(HttpServletRequest request) {
        if (!HttpClient.hasCookies(request)) {
            return null;
        }
        return HttpClient.getCookies(request)[0].getValue();
    }

    public static String getRemoteRealIp() {
        HttpServletRequest request = HttpClient.request;
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        HttpClient.restTemplate = restTemplate;
    }

}
