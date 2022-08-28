package com.sxs.reggie.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sxs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sxs
 * @create 2022-08-19 22:03
 *
 * TODO 前端的请求只有是配置过的url 才能进入interceptor
 *      o.s.web.servlet.PageNotFound : No mapping for GET /employee/page
 */
@Slf4j
//@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String[] URIS = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
        };
        if (check(URIS, requestURI)) {
            log.info("放行（匹配）{}",requestURI);
            return true;
        } else {
            Object o = request.getSession().getAttribute("employee");
            if (o != null) {
                log.info("放行（已登录）{}",requestURI);
                return true;
            } else {
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
                log.info("放行（未登录）{}",requestURI);
                return false;
            }
        }

    }

    private boolean check(String[] uris, String requestURI) {
        for (String s : uris) {
            if (PATH_MATCHER.match(s,requestURI)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
