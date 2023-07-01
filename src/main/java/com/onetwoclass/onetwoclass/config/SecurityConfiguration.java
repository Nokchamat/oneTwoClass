package com.onetwoclass.onetwoclass.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.exception.CustomAccessDeniedHandler;
import com.onetwoclass.onetwoclass.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

  private final JwtTokenProvider jwtTokenProvider;

  private final ObjectMapper objectMapper;

  @Bean
  protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity

        .csrf().disable()

        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
        .antMatchers("/api/v1/seller/**").hasRole(Role.SELLER.toString())
        .antMatchers("/api/v1/member/signup", "/api/v1/member/signin").permitAll()

        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))

        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

}
