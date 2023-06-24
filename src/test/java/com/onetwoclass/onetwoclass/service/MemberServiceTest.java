package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.SignUpForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MemberServiceTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("회원가입 성공")
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
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUNT_MEMBER));

    assertEquals(signUpForm.getEmail(), user.getEmail());
    assertEquals(signUpForm.getName(), user.getName());
    assertEquals(signUpForm.getPhone(), user.getPhone());
    assertEquals(signUpForm.getRole(), user.getRole());
    assertTrue(passwordEncoder.matches(signUpForm.getPassword(), user.getPassword()));

  }

  @Test
  @DisplayName("회원가입 실패 - 이메일 중복")
  void signUpFailSameEmailTest() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("testemail3@naver.com")
        .name("홍길동")
        .password("1234")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build();

    //when
    memberService.signUp(signUpForm);

    //then
    CustomException customException = assertThrows(
        CustomException.class, () -> memberService.signUp(signUpForm));

    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_EMAIL);
  }


  @Test
  @DisplayName("로그인 성공")
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