package com.example.springbootpractice.config.log;

import static com.example.springbootpractice.util.CommonConstants.REQUEST_ID_HEADER;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class ResponseHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestId = MDC.get(REQUEST_ID_HEADER);

        if (StringUtils.hasText(requestId)) {
            httpServletResponse.setHeader(REQUEST_ID_HEADER, requestId);
        }

        chain.doFilter(request, response);
    }
}
