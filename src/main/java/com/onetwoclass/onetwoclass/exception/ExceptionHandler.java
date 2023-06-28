package com.onetwoclass.onetwoclass.exception;

import com.onetwoclass.onetwoclass.exception.CustomException.CustomExceptionResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler({CustomException.class})
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

  @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<?> methodValidException(MethodArgumentNotValidException e
      , HttpServletRequest request) {

    System.out.println(e.getBindingResult().getAllErrors());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(e.getBindingResult().getAllErrors().stream()
            .map(a -> a.getDefaultMessage()));
  }

}
