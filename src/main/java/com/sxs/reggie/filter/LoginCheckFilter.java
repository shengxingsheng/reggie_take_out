package com.sxs.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.sxs.reggie.common.BaseContext;
import com.sxs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sxs
 * @create 2022-08-19 20:33
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = {"/*"})
public class LoginCheckFilter implements Filter {
    //TODO 路径匹配器
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI();
        //不需要处理的请求路径
        String[] URIS = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/logout",
                "/user/sendMsg"

        };
        //1.判断是否匹配规则
        if (check(URIS,requestURI)) {
            log.info("放行（匹配规则）：{}",request.getRequestURI());
            filterChain.doFilter(request,response);
            return;
        }
        //2.判断employee是否登录
        Long employeeId = (Long)request.getSession().getAttribute("employee");
        if (employeeId!=null){
            BaseContext.setCurrentId(employeeId);
            log.info("放行（已登陆）：{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //3.判断user 判断登录状态，如果已登录，则直接放行
        Long userId = (Long) request.getSession().getAttribute("user");
        if(userId != null){
            BaseContext.setCurrentId(userId);
            log.info("放行（已登陆）:{}", requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4.未登录的请求
        log.info("拦截（未登陆）：{}",request.getRequestURI());
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean check(String[] URIS, String requestURI) {
        for (String uri : URIS) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }


}
