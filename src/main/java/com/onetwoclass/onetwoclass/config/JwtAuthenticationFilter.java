package com.onetwoclass.onetwoclass.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("======= JwtAuthenticationFilter 동작 =======");

    String token = jwtTokenProvider.resolveToken(request);
    log.info("token 인증 시작 : {} ", token);

    if (token != null && !jwtTokenProvider.validateToken(token)) {
      Authentication authentication = jwtTokenProvider.getAuthentication(token);

      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.info("token 인증 완료 : {} ", token);
    }

    log.info("======= JwtAuthenticationFilter 완료 =======");
    filterChain.doFilter(request, response);
  }
}
