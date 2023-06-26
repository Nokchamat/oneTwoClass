package com.onetwoclass.onetwoclass.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CustomException extends RuntimeException {

  private ErrorCode errorCode;
  private final int httpStatus;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getDetailMessage());
    this.errorCode = errorCode;
    this.httpStatus = errorCode.getHttpStatus().value();
  }

  @AllArgsConstructor
  @Builder
  @NoArgsConstructor
  @Getter
  public static class CustomExceptionResponse {

    private int httpStatus;
    private String errorCode;
    private String detailMessage;
  }

}
