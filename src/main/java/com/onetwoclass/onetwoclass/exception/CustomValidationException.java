package com.onetwoclass.onetwoclass.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.ObjectError;

@Getter
public class CustomValidationException extends RuntimeException {

  private ErrorCode errorCode;
  private final int httpStatus;
  private List<ObjectError> objectErrors;

  public CustomValidationException(ErrorCode errorCode, List<ObjectError> objectErrors) {
    super(errorCode.getDetailMessage());
    this.errorCode = errorCode;
    this.httpStatus = errorCode.getHttpStatus().value();
    this.objectErrors = objectErrors;
  }

  @AllArgsConstructor
  @Builder
  @NoArgsConstructor
  @Getter
  public static class CustomValidationExceptionResponse {

    private int httpStatus;
    private String errorCode;
    private String detailMessage;
    private List<String> objectErrors;
  }

}
