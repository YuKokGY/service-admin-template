/**
 * @作者 YuKok
 * @创建时间 2020/10/26 17:27
 */
package io.github.talelin.latticy.common.interceptor;

import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.latticy.common.exception.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private DoubleJWT jwt;
    private final String[] excludeMethods = new String[]{"OPTIONS"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (checkInExclude(request.getMethod())) {
            // 有些请求方法无需检测，如OPTIONS
            return true;
        }
        //声明接口无需登陆即可访问
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            NotLogin notLogin = method.getAnnotation(NotLogin.class);
            if (notLogin != null) {
                return true;
            }
        }
        //从header中获取token
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            throw new ResponseException("缺少token，拒绝访问");
        } else {
            try {
                String[] splits = bearerToken.split(" ");
                if (splits.length != 2) {
                    throw new ResponseException(10013);
                }
                jwt.decodeAccessToken(splits[1]);
                return true;
            } catch (Exception e) {
                throw new ResponseException(10050);
            }
        }
    }
    private boolean checkInExclude(String method) {
        for (String excludeMethod : excludeMethods) {
            if (method.equals(excludeMethod)) {
                return true;
            }
        }
        return false;
    }
}
