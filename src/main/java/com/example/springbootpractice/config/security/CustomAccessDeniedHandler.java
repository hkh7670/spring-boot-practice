package com.example.springbootpractice.config.security;

import static com.example.springbootpractice.util.CommonConstant.REQUEST_ID_HEADER;

import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {

        log.error("접근 권한 없음: {}", accessDeniedException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader(REQUEST_ID_HEADER, MDC.get(REQUEST_ID_HEADER));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),
            new ErrorResponse(ErrorCode.AUTHORIZATION_FAIL));
    }
}