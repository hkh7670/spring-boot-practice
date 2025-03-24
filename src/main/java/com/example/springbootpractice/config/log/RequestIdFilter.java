package com.example.springbootpractice.config.log;

import static com.example.springbootpractice.util.CommonConstant.REQUEST_ID_HEADER;

import com.example.springbootpractice.util.CommonUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class RequestIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 클라이언트가 Request ID를 보내지 않으면 UUID 생성
        String requestId = httpServletRequest.getHeader(REQUEST_ID_HEADER);
        if (!StringUtils.hasText(requestId)) {
            requestId = CommonUtil.getRequestId();
        }

        try {
            MDC.put(REQUEST_ID_HEADER, requestId); // MDC에 저장하여 로그에서 활용
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // 요청 종료 후 MDC 정리
        }
    }
}