package com.example.springbootpractice.service;

import com.example.springbootpractice.config.security.JwtTokenProvider;
import com.example.springbootpractice.config.security.SecurityUtil;
import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.model.dto.AdultWebtoonViewersResponse;
import com.example.springbootpractice.model.dto.LogInRequest;
import com.example.springbootpractice.model.dto.SignUpRequest;
import com.example.springbootpractice.model.entity.UserEntity;
import com.example.springbootpractice.repository.UserRepository;
import com.example.springbootpractice.repository.WebtoonEvaluationRepository;
import com.example.springbootpractice.repository.WebtoonViewHistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final WebtoonEvaluationRepository webtoonEvaluationRepository;
  private final WebtoonViewHistoryRepository webtoonViewHistoryRepository;

  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;


  @Transactional(readOnly = true)
  public UserEntity getUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ApiErrorException(ErrorCode.NOT_FOUND_USER));
  }

  @Transactional
  public long createUser(SignUpRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new ApiErrorException(ErrorCode.DUPLICATED_EMAIL);
    }
    return userRepository.save(
        UserEntity.of(request, this.passwordEncoder.encode(request.password()))
    ).getSeq();
  }

  @Transactional(readOnly = true)
  public String getJwtToken(LogInRequest request) {
    UserEntity user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new ApiErrorException(ErrorCode.NOT_FOUND_USER));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new ApiErrorException(ErrorCode.INCORRECT_PASSWORD);
    }

    return jwtTokenProvider.createToken(user.getEmail(), user.getRole());
  }

  @Transactional
  public void deleteMyself() {
    UserEntity user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
        .orElseThrow(() -> new ApiErrorException(ErrorCode.NOT_FOUND_USER));
    long userSeq = user.getSeq();

    // 웹툰 조회 내역 삭제
    webtoonViewHistoryRepository.deleteByUser(user);
    // 웹툰 평가 내역 삭제
    webtoonEvaluationRepository.deleteByUserSeq(userSeq);
    // 유저 삭제
    userRepository.deleteById(userSeq);
  }

  @Transactional(readOnly = true)
  public Page<AdultWebtoonViewersResponse> getAdultWebtoonViewers(
      LocalDateTime from,
      LocalDateTime to,
      Pageable pageable
  ) {
    Page<UserEntity> page = userRepository.findAdultWebtoonViewers(from, to, pageable);
    return page.map(AdultWebtoonViewersResponse::from);
  }

}
