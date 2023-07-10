package com.onetwoclass.onetwoclass.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.debug("======= JwtAuthenticationFilter 동작 =======");

    String token = JwtTokenProvider.resolveToken(request);
    log.info("token 인증 시작 : {} ", token);

    try {

      if (token != null && !jwtTokenProvider.validateToken(token)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("token 인증 완료 : {} ", token);
      }
    } catch (ExpiredJwtException e) {
      setErrorResponse(request, response, e);
    }

    log.debug("======= JwtAuthenticationFilter 완료 =======");
    filterChain.doFilter(request, response);
  }

  public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws IOException {

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> body = new HashMap<>();
    body.put("status", ErrorCode.EXPIRED_TOKEN.getHttpStatus());
    body.put("errorCode", ErrorCode.EXPIRED_TOKEN);
    body.put("message", ErrorCode.EXPIRED_TOKEN.getDetailMessage());

    objectMapper.writeValue(response.getOutputStream(), body);

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

  }

}
