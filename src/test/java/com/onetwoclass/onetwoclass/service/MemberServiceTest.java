package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.sign.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.sign.SignUpForm;
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
  void Success_signUp() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Success_signUp@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build();

    //when
    memberService.signUp(signUpForm);

    //then
    Member user = memberRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    assertEquals(signUpForm.getEmail(), user.getEmail());
    assertEquals(signUpForm.getName(), user.getName());
    assertEquals(signUpForm.getPhone(), user.getPhone());
    assertEquals(signUpForm.getRole(), user.getRole());
    assertTrue(passwordEncoder.matches(signUpForm.getPassword(), user.getPassword()));

  }

  @Test
  @DisplayName("회원가입 실패 - 이메일 중복")
  void Fail_signUp_SameEmailTest() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Fail_signUp_SameEmailTest@tset.com")
        .name("홍길동")
        .password("12345678")
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
  void Success_signIn() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Success_signIn@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build();
    memberService.signUp(signUpForm);

    SignInForm signInForm = SignInForm.builder()
        .email(signUpForm.getEmail())
        .password(signUpForm.getPassword())
        .build();

    //when
    String token = memberService.signIn(signInForm);

    //then
    assertNotNull(token);
  }

  @Test
  @DisplayName("로그인 실패 - 멤버 없음")
  void Fail_signUp_NotFoundMember() {
    //given
    SignInForm signInForm = SignInForm.builder()
        .email("Fail_signUp_NotFoundMember@test.com")
        .password("12345678")
        .build();

    //when
    //then
    CustomException customException = assertThrows(
        CustomException.class, () -> memberService.signIn(signInForm));

    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_MEMBER);
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호 틀림")
  void Fail_signUp_MismatchedPasswordAndId() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Fail_signUp_MismatchedPasswordAndId@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build();

    SignInForm signInForm = SignInForm.builder()
        .email(signUpForm.getEmail())
        .password("12345678910")
        .build();

    memberService.signUp(signUpForm);

    //when
    CustomException customException = assertThrows(
        CustomException.class, () -> memberService.signIn(signInForm));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.MISMATCHED_PASSWORD_AND_ID);
  }

}