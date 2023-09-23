package com.example.bookmarketpassport.fitter;

import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    public JwtAuthenticationTokenFilter() {
        log.debug("创建配置类对象：JwtAuthenticationTokenFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //判定header里面的token
        //没token放行
        //从token解析uid，查用户权限信息
        String uid = "123456789";
        LoginUser loginUser = RedisUtils.getCacheObject(uid);
        if (loginUser == null) {
            log.error("没有用户：{}登录信息，不允许访问", uid);
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, loginUser.getUser().getPassword(), loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);//添加到SecurityContext上下文
        log.debug("用户：{}登录信息存在，允许访问，登录信息存放到SecurityContextHolder：{}", uid, authenticationToken);
        filterChain.doFilter(request, response);
    }
}
