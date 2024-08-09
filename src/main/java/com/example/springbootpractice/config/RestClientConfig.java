//package com.example.springbootpractice.config;
//
//import com.example.springbootpractice.exception.ApiErrorException;
//import com.example.springbootpractice.exception.ErrorCode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClient.ResponseSpec;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class RestClientConfig {
//
//  private static final String SCRAP_REST_CLIENT = "scrapRestClient";
//  private static final ObjectMapper objectMapper = new ObjectMapper();
//
//
//  @Bean(SCRAP_REST_CLIENT)
//  public RestClient getScrapRestClient() {
//    return RestClient.builder()
//        .baseUrl("https://codetest-v4.3o3.co.kr")
//        .defaultHeaders(httpHeaders -> {
//          httpHeaders.add("X-API-KEY", "fQL4PtoGRRgj6Q1OZG1WpQ==");
//          httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        })
//        .requestFactory(getClientHttpRequestFactory())
//        .defaultStatusHandler(HttpStatusCode::is4xxClientError, default4xxErrorHandler())
//        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
//          throw new ApiErrorException(ErrorCode.EXTERNAL_SERVER_ERROR);
//        })
//        .build();
//  }
//
//  private ClientHttpRequestFactory getClientHttpRequestFactory() {
//    var factory = new SimpleClientHttpRequestFactory();
//    factory.setReadTimeout(25 * 1000);
//    factory.setConnectTimeout(5 * 1000);
//    return factory;
//
//  }
//
//  private ResponseSpec.ErrorHandler default4xxErrorHandler() {
//    return (req, res) -> {
//      var errorBody = objectMapper.readValue(res.getBody(), Map.class);
//      log.debug(errorBody.toString());
//      int statusCode = res.getStatusCode().value();
//      switch (statusCode) {
//        case 401 -> throw new ApiErrorException(ErrorCode.UNAUTHORIZED);
//        case 404 -> throw new ApiErrorException(ErrorCode.NOT_FOUND);
//        default -> throw new ApiErrorException(ErrorCode.BAD_REQUEST);
//      }
//    };
//  }
//
//}
