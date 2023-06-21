package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.MemberSignInDto;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.SignUpForm;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository userRepository;

  public void signUp(SignUpForm signUpForm) {

    userRepository.save(Member.builder()
        .email(signUpForm.getEmail())
        .name(signUpForm.getName())
        .password(signUpForm.getPassword())
        .phone(signUpForm.getPhone())
        .role(signUpForm.getRole())
        .registeredAt(LocalDateTime.now())
        .modifiedAt(LocalDateTime.now())
        .build()
    );

  }

  public String signIn(SignInForm signInForm) {

    //Todo Custom Exception 구현 필요
    MemberSignInDto memberSignInDto = Member.toMemberSignInDto(
        userRepository.findByEmail(signInForm.getEmail())
            .orElseThrow(() -> new RuntimeException("유저가 없습니다."))
    );

    if (!memberSignInDto.getPassword().equals(signInForm.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    return "token";
  }


}
