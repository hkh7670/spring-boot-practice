package com.example.springbootpractice.config;

import com.example.springbootpractice.client.TMDBRestClient;
import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TMDBRestClientConfig {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${tmdb.api-read-access-token}")
  private String API_READ_ACCESS_TOKEN;

  @Value("${tmdb.api-key}")
  private String API_KEY;

  private static final int CONNECTION_TIMEOUT = 5 * 1000;
  private static final int READ_TIMEOUT = 25 * 1000;

  @Bean
  public TMDBRestClient tmdbRestClient() {
    return HttpServiceProxyFactory
        .builderFor(RestClientAdapter.create(getTMDBRestClient()))
        .build()
        .createClient(TMDBRestClient.class);
  }

  private RestClient getTMDBRestClient() {
    return RestClient.builder()
        .baseUrl("https://api.themoviedb.org/3")
        .defaultHeaders(httpHeaders -> {
          httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + API_READ_ACCESS_TOKEN);
          httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        })
        .requestFactory(getClientHttpRequestFactory())
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, default4xxErrorHandler())
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, default5xxErrorHandler())
        .build();
  }

  private ClientHttpRequestFactory getClientHttpRequestFactory() {
    var factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(CONNECTION_TIMEOUT);
    factory.setReadTimeout(READ_TIMEOUT);
    return factory;

  }

  private ResponseSpec.ErrorHandler default4xxErrorHandler() {
    return (req, res) -> {
      var errorBody = objectMapper.readValue(res.getBody(), Map.class);
      log.debug(errorBody.toString());
      int statusCode = res.getStatusCode().value();
      switch (statusCode) {
        case 401 -> throw new ApiErrorException(ErrorCode.AUTHENTICATION_FAIL);
        case 404 -> throw new ApiErrorException(ErrorCode.NOT_FOUND);
        default -> throw new ApiErrorException(ErrorCode.BAD_REQUEST);
      }
    };
  }

  private ResponseSpec.ErrorHandler default5xxErrorHandler() {
    return (req, res) -> {
      throw new ApiErrorException(ErrorCode.EXTERNAL_SERVER_ERROR);
    };
  }

}
