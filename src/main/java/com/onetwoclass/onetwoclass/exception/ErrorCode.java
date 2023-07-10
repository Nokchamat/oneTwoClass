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
  NOT_FOUND_DAYCLASS_SCHEDULER(HttpStatus.BAD_REQUEST, "데이클래스 스케쥴러가 존재하지 않습니다."),
  ALREADY_EXIST_DAYCLASS_SCHEDULER(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 스케쥴러 입니다."),
  MISMATCHED_SELLER_AND_DAYCLASS(HttpStatus.BAD_REQUEST, "데이클래스와 소유주가 다릅니다."),


  ALREADY_REQUESTED_SCHEDULE(HttpStatus.BAD_REQUEST, "이미 요청한 스케쥴 입니다."),
  ALREADY_ACCEPTED_SCHEDULE(HttpStatus.BAD_REQUEST, "이미 처리된 스케쥴 입니다."),
  NOT_FOUND_SCHEDULE(HttpStatus.BAD_REQUEST, "스케쥴이 존재하지 않습니다."),
  NOT_VISITED_DAYCLASS(HttpStatus.BAD_REQUEST, "방문한 데이클래스가 아닙니다."),

  NOT_FOUND_STORE_BOOKMARK(HttpStatus.BAD_REQUEST, "상점 북마크가 존재하지 않습니다."),
  ALREADY_EXIST_STORE_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 북마크에 등록되어 있는 상점 입니다."),

  ALREADY_EXIST_DAYCLASS_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 북마크에 등록되어 있는 데이클래스 입니다."),
  NOT_FOUND_DAYCLASS_BOOKMARK(HttpStatus.BAD_REQUEST, "데이클래스 북마크가 존재하지 않습니다."),


  ACCESS_DENIED(HttpStatus.FORBIDDEN, "잘못된 접근입니다."),
  WRONG_TYPE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

  ALREADY_EXIST_REVIEW(HttpStatus.BAD_REQUEST, "이미 작성된 리뷰가 존재합니다."),

  ALREADY_EXIST_CHATTINGROOM(HttpStatus.BAD_REQUEST, "이미 생성된 채팅방이 존재합니다."),
  NOT_FOUND_CHATTINGROOM(HttpStatus.BAD_REQUEST, "채팅방이 존재하지 않습니다."),

  NOT_EXIST_OTHERS(HttpStatus.BAD_REQUEST, "상대방이 채팅방에 존재하지 않습니다."),

  NOT_FOUND_NOTICE(HttpStatus.BAD_REQUEST, "공지사항이 존재하지 않습니다.");


  private final HttpStatus httpStatus;
  private final String detailMessage;

}
