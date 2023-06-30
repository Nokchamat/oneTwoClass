package com.onetwoclass.onetwoclass.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


  ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 해당 이메일로 회원가입한 계정이 있습니다."),
  MISMATCHED_PASSWORD_AND_ID(HttpStatus.BAD_REQUEST, "이메일과 비밀번호가 일치하지 않습니다."),
  NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."),
  PLEASE_CHECK_INFORMATION(HttpStatus.BAD_REQUEST, "입력 정보를 확인해주세요."),


  NOT_FOUND_STORE(HttpStatus.BAD_REQUEST, "상점이 존재하지 않습니다."),
  ALREADY_EXIST_STORE(HttpStatus.BAD_REQUEST, "이미 상점이 등록되어 있습니다."),
  PLEASE_DELETE_DAYCLASS_FIRST(HttpStatus.BAD_REQUEST, "남아있는 데이클래스가 있습니다."),

  DUPLICATION_DAYCLASS_NAME(HttpStatus.BAD_REQUEST, "중복된 데이클래스 이름이 존재합니다."),
  NOT_FOUND_DAYCLASS(HttpStatus.BAD_REQUEST, "데이클래스가 존재하지 않습니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "잘못된 접근입니다."),
  WRONG_TYPE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다.")

  ;

  private final HttpStatus httpStatus;
  private final String detailMessage;

}
