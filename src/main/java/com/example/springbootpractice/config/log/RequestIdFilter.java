package com.example.springbootpractice.config.log;

import static com.example.springbootpractice.util.CommonConstants.REQUEST_ID_HEADER;
import static com.example.springbootpractice.util.CommonConstants.THREAD_ID;
import static com.example.springbootpractice.util.CustomStringUtils.hasNotText;

import com.example.springbootpractice.util.CommonUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class RequestIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        long startTime = System.currentTimeMillis(); // 요청 시작 시간
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 클라이언트가 Request ID를 보내지 않으면 UUID 생성
        String requestId = httpServletRequest.getHeader(REQUEST_ID_HEADER);
        if (hasNotText(requestId)) {
            requestId = CommonUtils.getRequestId();
        }
        String threadId = Thread.currentThread().isVirtual()
            ? "virtual-thread-" + Thread.currentThread().threadId()
            : Thread.currentThread().getName();

        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();

        try {
            MDC.put(THREAD_ID, threadId);
            MDC.put(REQUEST_ID_HEADER, requestId); // MDC에 저장하여 로그에서 활용
            log.info("[{} {}] 요청 시작", method, uri);
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("[{} {}] 요청 완료 ({} ms)", method, uri, duration);
            MDC.clear(); // 요청 종료 후 MDC 정리
        }
    }
}