package com.onetwoclass.onetwoclass.exception;

import com.onetwoclass.onetwoclass.exception.CustomException.CustomExceptionResponse;
import com.onetwoclass.onetwoclass.exception.CustomValidationException.CustomValidationExceptionResponse;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomExceptionResponse> customExceptionHandler(
      CustomException customException) {
    return ResponseEntity
        .status(customException.getHttpStatus())
        .body(
            CustomExceptionResponse.builder()
                .httpStatus(customException.getHttpStatus())
                .detailMessage(customException.getMessage())
                .errorCode(customException.getErrorCode().name())
                .build());
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(CustomValidationException.class)
  public ResponseEntity<CustomValidationExceptionResponse> customValidationExceptionHandler(
      CustomValidationException customValidationException) {
    return ResponseEntity
        .status(customValidationException.getHttpStatus())
        .body(CustomValidationExceptionResponse.builder()
            .httpStatus(customValidationException.getHttpStatus())
            .detailMessage(customValidationException.getMessage())
            .errorCode(customValidationException.getErrorCode().name())
            .objectErrors(customValidationException.getObjectErrors().stream()
                .map(a -> a.getDefaultMessage())
                .collect(Collectors.toList()))
            .build());
  }
}
