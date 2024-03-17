package com.example.springbootpractice.service;

import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.model.dto.ImageResultResponse;
import com.example.springbootpractice.model.dto.ImageResultResponse.ImageResult;
import com.example.springbootpractice.model.entity.ImageInfo;
import com.example.springbootpractice.repository.ImageInfoJdbcRepository;
import com.example.springbootpractice.repository.ImageInfoRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final RestClientService restClientService;
  private final ImageInfoRepository imageInfoRepository;
  private final ImageInfoJdbcRepository imageInfoJdbcRepository;

  @Transactional(readOnly = true)
  public Page<ImageInfo> getImageInfos(Pageable pageable, String title) {
    return imageInfoRepository.findAllByTitleLike(pageable,
        String.format("%%%s%%", title == null ? "" : title));
  }

  @Transactional(readOnly = true)
  public ImageInfo getImageInfo(Long id) {
    return imageInfoRepository.findById(id)
        .orElseThrow(() -> new ApiErrorException(ErrorCode.NOT_FOUND));
  }

  @Transactional
  public void insertImageInfos() {
    ImageResultResponse data = restClientService.getImageResults()
        .orElseThrow(() -> new ApiErrorException(ErrorCode.NOT_FOUND));
    if (CollectionUtils.isEmpty(data.imageResults())) {
      throw new ApiErrorException(ErrorCode.NOT_FOUND);
    }

    List<ImageInfo> imageInfoList = new ArrayList<>();
    for (ImageResult res : data.imageResults()) {
      imageInfoList.add(ImageInfo.of(res));
    }
    // JPA로 insert
    imageInfoRepository.saveAll(imageInfoList);
    // jdbcTemplate으로 insert
    imageInfoJdbcRepository.saveAll(imageInfoList);

  }
}
