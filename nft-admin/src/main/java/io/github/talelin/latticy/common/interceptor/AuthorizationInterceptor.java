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

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private DoubleJWT jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Token annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Token.class);
        } else {
            return true;
        }
        //没有声明需要权限,或者声明不验证权限
        if (annotation == null || !annotation.validate()) {
            return true;
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
                throw new ResponseException(10040);
            }
        }
    }
}
