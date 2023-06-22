package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.SignUpForm;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Test
  void signUpTest() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("testemail2@naver.com")
        .name("홍길동")
        .password("1234")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build();

    //when
    memberService.signUp(signUpForm);

    //then
    Member user = memberRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new RuntimeException(""));

    assertEquals(signUpForm.getEmail(), user.getEmail());
    assertEquals(signUpForm.getName(), user.getName());
    assertEquals(signUpForm.getPassword(), user.getPassword());
    assertEquals(signUpForm.getPhone(), user.getPhone());
    assertEquals(signUpForm.getRole(), user.getRole());

  }

  @Test
  void signInTest() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("testemail@naver.com")
        .name("홍길동")
        .password("1234")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build();
    memberService.signUp(signUpForm);

    SignInForm signInForm = SignInForm.builder()
        .email("testemail@naver.com")
        .password("1234")
        .build();

    //when
    String token = memberService.signIn(signInForm);

    //then
    assertEquals("token", token);

  }

}