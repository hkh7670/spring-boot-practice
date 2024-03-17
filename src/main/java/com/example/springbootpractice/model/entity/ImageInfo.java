package com.example.springbootpractice.model.entity;

import com.example.springbootpractice.model.dto.ImageResultResponse.ImageResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "IMAGE_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ImageInfo extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long position;
  private String thumbnail;
  @Column(name = "RELATED_CONTENT_ID")
  private String relatedContentId;
  @Column(name = "SERPAPI_RELATED_CONTENT_LINK")
  private String serpapiRelatedContentLink;
  private String source;
  @Column(name = "SOURCE_LOGO")
  private String sourceLogo;
  private String title;
  private String link;
  private String original;
  @Column(name = "ORIGINAL_WIDTH")
  private Integer originalWidth;
  @Column(name = "ORIGINAL_HEIGHT")
  private Integer originalHeight;
  @Column(name = "IS_PRODUCT")
  Boolean isProduct;

  public static ImageInfo of(ImageResult imageResult) {
    return ImageInfo.builder()
        .position(imageResult.position())
        .thumbnail(imageResult.thumbnail())
        .relatedContentId(imageResult.relatedContentId())
        .serpapiRelatedContentLink(imageResult.serpapiRelatedContentLink())
        .source(imageResult.source())
        .sourceLogo(imageResult.sourceLogo())
        .title(imageResult.title())
        .link(imageResult.link())
        .original(imageResult.original())
        .originalWidth(imageResult.originalWidth())
        .originalHeight(imageResult.originalHeight())
        .isProduct(imageResult.isProduct())
        .build();
  }

}
