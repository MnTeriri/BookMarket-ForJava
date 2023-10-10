package com.example.bookmarketpassport.fitter;

import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.utils.JwtUtils;
import com.example.bookmarketpassport.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
        String token = request.getHeader("token");
        System.out.println(request.getMethod());
        if (token == null) {
            log.error("没有token，不允许访问");
            filterChain.doFilter(request, response);//没token放行
            return;
        }
        //从token解析uid，查用户权限信息
        Claims claims = JwtUtils.parseJWT(token);
        String uid = claims.getSubject();
        //从redis中读取信息
        LoginUser loginUser = RedisUtils.getCacheObject(uid, LoginUser.class);
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
