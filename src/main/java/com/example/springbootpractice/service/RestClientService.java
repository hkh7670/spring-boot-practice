package com.example.springbootpractice.service;

import com.example.springbootpractice.model.dto.ImageResultResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RestClientService {

  private final RestClient restClient;

  public Optional<ImageResultResponse> getImageResults() {
    return Optional.ofNullable(restClient.get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("q", "Apple")
            .queryParam("engine", "google_images")
            .queryParam("ijn", "0")
            .build())
        .retrieve()
        .toEntity(ImageResultResponse.class)
        .getBody());
  }

}
