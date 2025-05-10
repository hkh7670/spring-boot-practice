package com.example.springbootpractice.config.security;

import static com.example.springbootpractice.config.security.SecurityConfig.PERMIT_ALL_URI;
import static com.example.springbootpractice.util.CommonConstants.AUTH_ERROR_CODE;

import com.example.springbootpractice.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            // PERMIT_ALL_URI에 해당하면 필터 로직 skip
            for (String pattern : PERMIT_ALL_URI) {
                if (pathMatcher.match(pattern, requestURI)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
            // 1. 헤더에서 JWT를 받아옴
            String authorizationHeaderValue = jwtTokenProvider.getAuthorizationHeaderValue(request);
            log.info("Authorization header: {}", authorizationHeaderValue);

            // 2. Bearer 토큰인지 체크
            if (jwtTokenProvider.isNotBearerToken(authorizationHeaderValue)) {
                log.error("Not bearer token");
                request.setAttribute(AUTH_ERROR_CODE, ErrorCode.INVALID_TOKEN);
                chain.doFilter(request, response);
                return;
            }

            // 3. 토큰의 유효성 검사
            String token = jwtTokenProvider.getToken(authorizationHeaderValue);
            log.info("Token: {}", token);
            if (jwtTokenProvider.isNotValidToken(token)) {
                log.error("Not valid token value");
                request.setAttribute(AUTH_ERROR_CODE, ErrorCode.INVALID_TOKEN);
                chain.doFilter(request, response);
                return;
            }

            // 4. 토큰 만료 유무 검사
            if (jwtTokenProvider.isExpiredToken(token)) {
                log.error("Not valid token value");
                request.setAttribute(AUTH_ERROR_CODE, ErrorCode.ACCESS_TOKEN_EXPIRED);
                chain.doFilter(request, response);
                return;
            }

            // 5. 토큰이 유효하면 토큰의 이메일 정보 기준으로 유저 정보를 DB에서 조회
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 6. SecurityContext에 Authentication 객체 설정 후 doFilter를 호출하여 다음 단계 진행
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            log.error("User Not Found");
            request.setAttribute(AUTH_ERROR_CODE, ErrorCode.AUTHENTICATION_FAIL);
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Authentication Failed");
            request.setAttribute(AUTH_ERROR_CODE, ErrorCode.AUTHENTICATION_FAIL);
            chain.doFilter(request, response);
        }
    }

}
