package com.example.springbootpractice.config;

import com.example.springbootpractice.client.TMDBRestClient;
import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
public class TMDBRestClientConfig {

    private final String API_READ_ACCESS_TOKEN;
    private final String API_KEY;

    public TMDBRestClientConfig(
        @Value("${tmdb.api-read-access-token}")
        String API_READ_ACCESS_TOKEN,
        @Value("${tmdb.api-key}")
        String API_KEY
    ) {
        this.API_READ_ACCESS_TOKEN = API_READ_ACCESS_TOKEN;
        this.API_KEY = API_KEY;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String REST_CLIENT_BEAN_NAME = "tmdbRestClient";

    private static final int CONNECTION_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 25;
    private static final int MAX_TOTAL_CONNECTIONS = 100;   // 전체 커넥션 수 최대값
    private static final int MAX_PER_ROUTE = 20;            // 도메인(호스트) 당 최대 커넥션 수
    private static final int MAX_RETRY_ATTEMPT_COUNT = 3;   // 최대 재시도 횟수
    private static final long DEFAULT_RETRY_INTERVAL = 1L;  // 재시도 간격 (초)
    private static final long MAX_IDLE_TIME = 30L;


    @Bean(name = REST_CLIENT_BEAN_NAME)
    public TMDBRestClient createClient() {
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(createRestClient()))
            .build()
            .createClient(TMDBRestClient.class);
    }

    private RestClient createRestClient() {
        return RestClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeaders(createHeadersConsumer())
            .requestFactory(createClientHttpRequestFactory())
            .requestInterceptor(loggingInterceptor())
            .defaultStatusHandler(HttpStatusCode::is4xxClientError, default4xxErrorHandler())
            .defaultStatusHandler(HttpStatusCode::is5xxServerError, default5xxErrorHandler())
            .build();
    }

    private Consumer<HttpHeaders> createHeadersConsumer() {
        return httpHeaders -> {
            httpHeaders.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeaderValue());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        };
    }

    private String getAuthorizationHeaderValue() {
        return "Bearer " + this.API_READ_ACCESS_TOKEN;
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        // loggingInterceptor 메소드에서 응답 Body를 InputStream 으로 한번 읽게 되면 실제 비즈니스 로직에서 해당 Body를 읽을 수 없다.
        // 따라서 BufferingClientHttpRequestFactory를 사용하여 응답 Body를 버퍼링하여 여러 번 읽을 수 있도록 한다.
        return new BufferingClientHttpRequestFactory(
            new HttpComponentsClientHttpRequestFactory(createApacheHttpClient())
        );
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            log.info("[TMDB Request] {} {}", request.getMethod(), request.getURI());
            log.debug("Headers: {}", request.getHeaders());
            if (body.length > 0) {
                log.debug("Request Body: {}", new String(body, StandardCharsets.UTF_8));
            }

            ClientHttpResponse response = execution.execute(request, body);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return response;
            }

            log.info("[TMDB Response] Status: {}", response.getStatusCode());
            String responseBody = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));
            log.info("Response Body: {}", responseBody);

            return response;
        };
    }


    private HttpClient createApacheHttpClient() {
        return HttpClients.custom()
            .setConnectionManager(createHttpClientConnectionManager())
            .setDefaultRequestConfig(createClientRequestConfig())
            .setRetryStrategy(createRetryStrategy())
            .evictIdleConnections(
                Timeout.ofSeconds(MAX_IDLE_TIME) // 일정 시간 이상 유휴 커넥션 정리 (메모리 누수 방지)
            )
            .build();
    }

    private HttpClientConnectionManager createHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        return connectionManager;
    }

    private RequestConfig createClientRequestConfig() {
        return RequestConfig.custom()
            .setConnectionRequestTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .setResponseTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build();
    }

    private HttpRequestRetryStrategy createRetryStrategy() {
        return new DefaultHttpRequestRetryStrategy(
            MAX_RETRY_ATTEMPT_COUNT,
            TimeValue.ofSeconds(DEFAULT_RETRY_INTERVAL)
        );
    }

    private ResponseSpec.ErrorHandler default4xxErrorHandler() {
        return (req, res) -> {
            Map<String, Object> errorBody = getErrorBody(res);
            HttpStatusCode statusCode = res.getStatusCode();
            printErrorLog(statusCode, errorBody);
            switch (statusCode) {
                case HttpStatus.UNAUTHORIZED ->
                    throw new ApiErrorException(ErrorCode.AUTHENTICATION_FAIL);
                case HttpStatus.NOT_FOUND -> throw new ApiErrorException(ErrorCode.NOT_FOUND);
                default -> throw new ApiErrorException(ErrorCode.BAD_REQUEST);
            }
        };
    }

    private ResponseSpec.ErrorHandler default5xxErrorHandler() {
        return (req, res) -> {
            Map<String, Object> errorBody = getErrorBody(res);
            HttpStatusCode statusCode = res.getStatusCode();
            printErrorLog(statusCode, errorBody);
            throw new ApiErrorException(ErrorCode.EXTERNAL_SERVER_ERROR);
        };
    }

    private Map<String, Object> getErrorBody(ClientHttpResponse res) {
        Map<String, Object> errorBody;
        try {
            errorBody = objectMapper.readValue(
                res.getBody(),
                new TypeReference<>() {
                }
            );
        } catch (Exception e) {
            log.error("Failed to parse TMDBRestClient error response body", e);
            throw new ApiErrorException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
        return errorBody;
    }

    private void printErrorLog(
        HttpStatusCode statusCode,
        Map<String, Object> errorBody
    ) {
        log.error("[TMDB Response] Error Status Code: {}", statusCode.value());
        log.error("[TMDB Response] Error Body: {}", errorBody);
    }

}
