package com.onetwoclass.onetwoclass.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

  NOT_FOUNT_MEMBER(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."),
  ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 해당 이메일로 회원가입한 계정이 있습니다."),
  MISMATCHED_PASSWORD_AND_ID(HttpStatus.BAD_REQUEST, "이메일과 비밀번호가 일치하지 않습니다."),

  PLEASE_CHECK_INFORMATION(HttpStatus.BAD_REQUEST, "입력 정보를 확인해주세요.");

  private final HttpStatus httpStatus;
  private final String detailMessage;

}
