package com.itjy.reggie.filter;
import com.alibaba.fastjson.JSON;
import com.itjy.reggie.common.BaseContext;
import com.itjy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
//        log.info("拦截: {}",req.getRequestURI());
        //本次请求的url
        String requestURI = req.getRequestURI();
        //不需要处理的请求
        String []urls = new String[] {
//                "/employee/",
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login"
        };
        log.info(requestURI);
        boolean check = check(urls, requestURI);
        if(check) {
            filterChain.doFilter(req,res);
            return;
        }

        //判断登录状态
        if ( req.getSession().getAttribute("employee") != null) {
            Long empId = (Long) req.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(req,res);
            return;
        }

        //移动端判断登录状态
        if ( req.getSession().getAttribute("user") != null) {
            Long userId = (Long) req.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(req,res);
            return;
        }

        res.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }

    public boolean check(String []urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }

}

