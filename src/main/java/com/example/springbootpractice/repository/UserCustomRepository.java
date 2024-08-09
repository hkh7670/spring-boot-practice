package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.dto.AdultWebtoonViewersResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {

  Page<AdultWebtoonViewersResponse> findAdultWebtoonViewers(LocalDateTime from, LocalDateTime to,
      Pageable pageable);
}
