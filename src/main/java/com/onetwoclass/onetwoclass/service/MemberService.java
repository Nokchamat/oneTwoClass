package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.sign.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.sign.SignUpForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public void signUp(SignUpForm signUpForm) {

    memberRepository.findByEmail(signUpForm.getEmail())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        });

    memberRepository.save(Member.builder()
        .email(signUpForm.getEmail())
        .name(signUpForm.getName())
        .password(passwordEncoder.encode(signUpForm.getPassword()))
        .phone(signUpForm.getPhone())
        .role(signUpForm.getRole())
        .build()
    );

  }

  public String signIn(SignInForm signInForm) {

    Member member = memberRepository.findByEmail(signInForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    if (!passwordEncoder.matches(signInForm.getPassword(), member.getPassword())) {
      throw new CustomException(ErrorCode.MISMATCHED_PASSWORD_AND_ID);
    }

    return jwtTokenProvider.createToken(String.valueOf(member.getEmail()), member.getRole());
  }

}
