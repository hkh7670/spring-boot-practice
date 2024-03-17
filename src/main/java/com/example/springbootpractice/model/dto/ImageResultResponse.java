package com.example.springbootpractice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ImageResultResponse(
    @JsonProperty("images_results")
    List<ImageResult> imageResults
) {

  public record ImageResult(
      Long position,
      String thumbnail,
      @JsonProperty("related_content_id")
      String relatedContentId,
      @JsonProperty("serpapi_related_content_link")
      String serpapiRelatedContentLink,
      String source,
      @JsonProperty("source_logo")
      String sourceLogo,
      String title,
      String link,
      String original,
      @JsonProperty("original_width")
      Integer originalWidth,
      @JsonProperty("original_height")
      Integer originalHeight,
      @JsonProperty("is_product")
      Boolean isProduct
  ) {

  }

}
