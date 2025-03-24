package com.example.springbootpractice.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        try {
            // 헤더에서 JWT를 받아옴
            String authorizationHeaderValue = jwtTokenProvider.getAuthorizationHeaderValue(request);
            if (jwtTokenProvider.isNotBearerToken(authorizationHeaderValue)) {
                return;
            }
            String token = jwtTokenProvider.getToken(authorizationHeaderValue);
            if (jwtTokenProvider.isNotValidToken(token)) {
                return;
            }

            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } finally {
            chain.doFilter(request, response);
        }

    }

}
