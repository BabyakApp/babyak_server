package com.babyak.babyak.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private RedisUtil redisUtil;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request);

        try {
            if(accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
//                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
//                SecurityContextHolder.getContext().setAuthentication(authentication);

                String isLogout = (String)redisUtil.getRedisLogoutAccTkn(accessToken);

                // 로그아웃하지 않은 사용자인 경우
                if(ObjectUtils.isEmpty(isLogout)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(401, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
