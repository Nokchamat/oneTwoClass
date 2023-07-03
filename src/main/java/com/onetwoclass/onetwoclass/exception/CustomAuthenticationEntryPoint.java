package com.onetwoclass.onetwoclass.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwoclass.onetwoclass.exception.CustomException.CustomExceptionResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    log.info("[CustomAuthenticationEntryPoint] 발생 token : {}", request.getHeader("X-AUTH-TOKEN"));

    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");

    CustomExceptionResponse exceptionResponse = CustomExceptionResponse.builder()
        .httpStatus(ErrorCode.WRONG_TYPE_TOKEN.getHttpStatus().value())
        .detailMessage(ErrorCode.WRONG_TYPE_TOKEN.getDetailMessage())
        .errorCode(ErrorCode.WRONG_TYPE_TOKEN.name())
        .build();

    String json = objectMapper.writeValueAsString(exceptionResponse);

    response.getWriter().write(json);
  }
}
