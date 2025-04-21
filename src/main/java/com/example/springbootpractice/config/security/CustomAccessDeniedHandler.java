package com.example.springbootpractice.config.security;

import static com.example.springbootpractice.util.CommonConstants.REQUEST_ID_HEADER;

import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {

        log.error("접근 권한 없음: {}", accessDeniedException.getMessage());

        String requestId = MDC.get(REQUEST_ID_HEADER);
        if (StringUtils.hasText(requestId)) {
            response.setHeader(REQUEST_ID_HEADER, requestId);
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        objectMapper.writeValue(
            response.getOutputStream(),
            ErrorResponse.from(ErrorCode.AUTHORIZATION_FAIL)
        );
    }
}