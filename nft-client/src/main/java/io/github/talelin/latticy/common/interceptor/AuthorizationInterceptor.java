/**
 * @作者 YuKok
 * @创建时间 2020/10/26 17:27
 */
package io.github.talelin.latticy.common.interceptor;

import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import io.github.talelin.latticy.common.exception.ResponseException;
import io.github.talelin.latticy.common.util.system.DoubleJWT;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String BEARER_PATTERN = "^Bearer$";
    private final String[] excludeMethods = new String[]{"OPTIONS"};
    @Autowired
    private DoubleJWT jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkInExclude(request.getMethod())) {
            // 有些请求方法无需检测，如OPTIONS
            return true;
        }
        //声明接口无需登陆即可访问
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            NotLogin notLogin = method.getAnnotation(NotLogin.class);
            if (notLogin == null) {
                return true;
            }
            return handleLogin(request, response);
        }
        return true;
    }

    public boolean handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String tokenStr = verifyHeader(request, response);
        Map<String, Claim> claims;
        try {
            claims = jwt.decodeAccessToken(tokenStr);
        } catch (TokenExpiredException e) {
            throw new ResponseException(e.getMessage(), 10051);
        } catch (AlgorithmMismatchException | SignatureVerificationException | JWTDecodeException |
                 InvalidClaimException e) {
            throw new ResponseException(e.getMessage(), 10041);
        }
        return getClaim(claims);
    }

    private boolean getClaim(Map<String, Claim> claims) {
        if (claims == null) {
            throw new ResponseException("token is invalid, can't be decode", 10041);
        }
//        int identity = claims.get("identity").asInt();
//        UserDO user = userService.getById(identity);
//        if (user == null) {
//            throw new NotFoundException("user is not found", 10021);
//        }
//        LocalUser.setLocalUser(user);
        return true;
    }

    private String verifyHeader(HttpServletRequest request, HttpServletResponse response) {
        // 处理头部header,带有access_token的可以访问
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization == null || Strings.isBlank(authorization)) {
            throw new ResponseException("authorization field is required", 10012);
        }
        String[] splits = authorization.split(" ");
        if (splits.length != 2) {
            throw new ResponseException("authorization field is invalid", 10013);
        }
        // Bearer 字段
        String scheme = splits[0];
        // token 字段
        String tokenStr = splits[1];
        if (!Pattern.matches(BEARER_PATTERN, scheme)) {
            throw new ResponseException("authorization field is invalid", 10013);
        }
        return tokenStr;
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
