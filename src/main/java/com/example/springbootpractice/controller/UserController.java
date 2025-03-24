package com.example.springbootpractice.controller;

import com.example.springbootpractice.model.dto.AdultWebtoonViewersResponse;
import com.example.springbootpractice.model.dto.LogInRequest;
import com.example.springbootpractice.model.dto.LogInResponse;
import com.example.springbootpractice.model.dto.PagingRequest;
import com.example.springbootpractice.model.dto.SignUpRequest;
import com.example.springbootpractice.model.dto.SignUpResponse;
import com.example.springbootpractice.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입
     *
     * @param request
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> requestSignUp(
        @RequestBody @Valid SignUpRequest request
    ) {
        log.info("회원가입 요청: {}", request.toString());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SignUpResponse.of(userService.createUser(request)));
    }

    /**
     * 토큰 발급
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public LogInResponse requestLogIn(
        @RequestBody @Valid LogInRequest request
    ) {
        return new LogInResponse(userService.getJwtToken(request));
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/me")
    public void requestDeleteMyself() {
        userService.deleteMyself();
    }

    /**
     * 최근 일주일 등록 사용자 중 성인작품 3개 이상 조회한 사용자 목록 조회 API (관리자용)
     *
     * @param request
     * @return
     */
    @GetMapping("/adult-webtoon-view")
    public Page<AdultWebtoonViewersResponse> requestAdultWebtoonViewers(
        @ModelAttribute PagingRequest request
    ) {
        return userService.getAdultWebtoonViewers(
            LocalDateTime.now().minusDays(7),
            LocalDateTime.now(),
            request.toPageable("seq")
        );
    }


}
