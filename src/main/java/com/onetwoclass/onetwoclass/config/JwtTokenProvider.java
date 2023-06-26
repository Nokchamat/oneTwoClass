package com.onetwoclass.onetwoclass.config;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final MemberRepository memberRepository;

  @Value("${jwt.secret}")
  private String secretKey = "oneTwoClass";

  @Value("#{${jwt.validate}}")
  private Long tokenValidTime = 1000L * 60 * 60; // 토큰 유효시간 1시간 기본

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String email, Role role) {

    Claims claims = Jwts.claims().setSubject(email);
    claims.put("roles", role);

    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidTime))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();

  }

  public Authentication getAuthentication(String token) {
    Member member = memberRepository.findByEmail(getMemberEmail(token))
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUNT_MEMBER));

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));

    return new UsernamePasswordAuthenticationToken("member", "", authorities);
  }

  public String getMemberEmail(String token) {
    return Jwts.parser().setSigningKey(secretKey)
        .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {

    Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

    return claims.getBody().getExpiration().before(new Date());

  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("X-AUTH-TOKEN");
  }


}
