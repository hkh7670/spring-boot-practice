package com.example.springbootpractice.config;

import com.example.springbootpractice.client.TMDBRestClient;
import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
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
    private static final int CONNECTION_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 25;

    private static final int MAX_TOTAL_CONNECTIONS = 100;
    private static final int MAX_PER_ROUTE = 20;

    @Bean
    public TMDBRestClient tmdbRestClient() {
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(getTMDBRestClient()))
            .build()
            .createClient(TMDBRestClient.class);
    }

    private RestClient getTMDBRestClient() {
        return RestClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.add(HttpHeaders.AUTHORIZATION, getAuthorizationHeaderValue());
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            })
//            .requestFactory(getClientHttpRequestFactory())
            .requestFactory(getClientHttpRequestFactory())
            .defaultStatusHandler(HttpStatusCode::is4xxClientError, default4xxErrorHandler())
            .defaultStatusHandler(HttpStatusCode::is5xxServerError, default5xxErrorHandler())
            .build();
    }

    private String getAuthorizationHeaderValue() {
        return "Bearer " + this.API_READ_ACCESS_TOKEN;
    }

//    private ClientHttpRequestFactory getClientHttpRequestFactory() {
//        var factory = new SimpleClientHttpRequestFactory();
//        factory.setConnectTimeout(CONNECTION_TIMEOUT);
//        factory.setReadTimeout(READ_TIMEOUT);
//        return factory;
//    }


    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(createApacheHttpClient());
    }

    private HttpClient createApacheHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .setResponseTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .evictIdleConnections(Timeout.ofSeconds(30))
            .build();
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
        log.error("TMDBRestClient Error Status Code: {}", statusCode.value());
        log.error("TMDBRestClient Error Body: {}", errorBody);
    }

}
