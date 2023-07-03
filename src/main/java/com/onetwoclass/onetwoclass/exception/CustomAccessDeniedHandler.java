package com.onetwoclass.onetwoclass.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwoclass.onetwoclass.exception.CustomException.CustomExceptionResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  // 액세스 권한이 없는 리소스에 접근할 경우 발생하는 예외
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.info("[CustomAccessDeniedHandler] 발생 token : {}", request.getHeader("X-AUTH-TOKEN"));

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("utf-8");

    CustomExceptionResponse exceptionResponse = CustomExceptionResponse.builder()
        .httpStatus(ErrorCode.ACCESS_DENIED.getHttpStatus().value())
        .detailMessage(ErrorCode.ACCESS_DENIED.getDetailMessage())
        .errorCode(ErrorCode.ACCESS_DENIED.name())
        .build();

    String json = objectMapper.writeValueAsString(exceptionResponse);

    response.getWriter().write(json);
  }

}
