package io.github.talelin.latticy.common.configuration.system;

import io.github.talelin.latticy.common.properties.CrossOriginProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CrossOriginConfiguration extends OncePerRequestFilter {

    @Autowired
    private CrossOriginProperties crossOriginProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (crossOriginProperties.getCrossOrigin()) {
            httpServletResponse.addHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
            httpServletResponse.addHeader("Access-Control-Allow-Headers", crossOriginProperties.getAllowHeaders());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

}


//if ($request_method = 'OPTIONS') {
//        add_header 'Access-Control-Allow-Origin' $http_origin;
//        add_header 'Access-Control-Allow-Credentials' 'true';
//        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
//        add_header 'Access-Control-Allow-Headers' 'DNT,web-token,app-token,Authorization,Accept,Origin,Keep-Alive,User-Agent,X-Mx-ReqToken,X-Data-Type,X-Auth-Token,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
//        add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range';
//        add_header 'Access-Control-Max-Age' 1728000;
//        add_header 'Content-Type' 'text/plain; charset=utf-8';
//        add_header 'Content-Length' 0;
//        return 204;
//        }