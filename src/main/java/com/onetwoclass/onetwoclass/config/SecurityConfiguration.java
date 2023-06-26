package com.onetwoclass.onetwoclass.config;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.exception.CustomAccessDeniedHandler;
import com.onetwoclass.onetwoclass.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity

        .csrf().disable()

        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
        .anyRequest()
        .hasRole(Role.SELLER.toString())

        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers("/api/v1/member/signin", "/api/v1/member/signup");
  }

}
