package com.example.springbootpractice.config;

import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

  private static final String REST_CLIENT = "restClient";
  private static final String NAVER_REST_CLIENT = "naverRestClient";

  private final ObjectMapper objectMapper;


  @Bean(REST_CLIENT)
  public RestClient getRestClient() {
    return RestClient.builder()
        .baseUrl("https://serpapi.com/search.json")
        .defaultHeader("Content-Type", "application/json")
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, default4xxErrorHandler())
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
          throw new ApiErrorException(ErrorCode.EXTERNAL_SERVER_ERROR);
        })
        .build();
  }

  @Bean(NAVER_REST_CLIENT)
  public RestClient getNaverRestClient() {
    return RestClient.builder()
        .baseUrl("https://naver.com")
        .defaultHeader("Content-Type", "application/json")
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, default4xxErrorHandler())
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
          throw new ApiErrorException(ErrorCode.EXTERNAL_SERVER_ERROR);
        })
        .build();
  }

  private ResponseSpec.ErrorHandler default4xxErrorHandler() {
    return (req, res) -> {
      var errorBody = objectMapper.readValue(res.getBody(), Map.class);
      log.debug(errorBody.toString());
      int statusCode = res.getStatusCode().value();
      switch (statusCode) {
        case 401 -> throw new ApiErrorException(ErrorCode.UNAUTHORIZED);
        case 404 -> throw new ApiErrorException(ErrorCode.NOT_FOUND);
        default -> throw new ApiErrorException(ErrorCode.BAD_REQUEST);
      }
    };
  }

}
