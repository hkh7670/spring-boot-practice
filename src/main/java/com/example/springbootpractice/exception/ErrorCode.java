package com.example.springbootpractice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // 4xx Error
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "허용되지 않은 요청 입니다."),
  SCHEMA_VALIDATE_ERROR(HttpStatus.BAD_REQUEST, "요청 필드에 대한 검증에 실패하였습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저정보를 찾을 수 없습니다."),
  NOT_FOUND_WEBTOON_INFO(HttpStatus.NOT_FOUND, "웹툰 작품정보를 찾을 수 없습니다."),
  NOT_ALLOWED_USER_FOR_SIGN_UP(HttpStatus.BAD_REQUEST, "가입이 허용 되지 않은 사용자 입니다."),
  NOT_ALLOWED_USER_TYPE_FOR_WEBTOON(HttpStatus.FORBIDDEN, "해당 웹툰에 대한 접근 권한이 없습니다."),
  DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
  ALREADY_EXIST_WEBTOON_EVALUATION_INFO(HttpStatus.BAD_REQUEST, "작품 평가정보가 이미 존재합니다."),
  INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

  // 인증 관련 Error
  AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "인증 되지 않은 사용자 입니다."),
  AUTHORIZATION_FAIL(HttpStatus.FORBIDDEN, "API 접근 권한이 없습니다."),

  // 500 Error
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류 입니다. 관리자 에게 문의 하세요."),
  EXTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 서버 오류 입니다. 관리자 에게 문의 하세요."),
  ;

  private final HttpStatus status;
  private final String message;
}
