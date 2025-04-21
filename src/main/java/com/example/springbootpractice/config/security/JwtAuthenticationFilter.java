package com.example.springbootpractice.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
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
            // 1. 헤더에서 JWT를 받아옴
            String authorizationHeaderValue = jwtTokenProvider.getAuthorizationHeaderValue(request);
            log.info("Authorization header: {}", authorizationHeaderValue);

            // 2. Bearer 토큰인지 체크
            if (jwtTokenProvider.isNotBearerToken(authorizationHeaderValue)) {
                log.error("Not bearer token");
                return;
            }

            // 3. 토큰의 유효성 검사
            String token = jwtTokenProvider.getToken(authorizationHeaderValue);
            log.info("Token: {}", token);
            if (jwtTokenProvider.isNotValidToken(token)) {
                log.error("Not valid token value");
                return;
            }

            // 4. 토큰이 유효하면 토큰의 이메일 정보 기준으로 유저 정보를 DB에서 조회
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 5. SecurityContext에 Authentication 객체 설정 후 doFilter를 호출하여 다음 단계 진행
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } finally {
            chain.doFilter(request, response);
        }

    }

}
