package com.example.springbootpractice.controller;

import com.example.springbootpractice.model.entity.ImageInfo;
import com.example.springbootpractice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

  private final ImageService imageService;

  @GetMapping
  public ResponseEntity<Page<ImageInfo>> getImageInfos(@PageableDefault Pageable pageable,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String regDate) {
    return ResponseEntity.ok().body(imageService.getImageInfos(pageable, title));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImageInfo> getImageInfo(@PathVariable Long id) {
    return ResponseEntity.ok().body(imageService.getImageInfo(id));
  }

  @PostMapping
  public ResponseEntity<Void> insertImageInfos() {
    imageService.insertImageInfos();
    return ResponseEntity.ok().build();
  }
}
