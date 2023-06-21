package com.onetwoclass.onetwoclass.controller;

import com.onetwoclass.onetwoclass.domain.form.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.SignUpForm;
import com.onetwoclass.onetwoclass.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService userService;

  @PostMapping("/signup")
  ResponseEntity<?> signUp(SignUpForm signUpForm) {

    userService.signUp(signUpForm);

    return ResponseEntity.ok(signUpForm.getName() + "님의 회원가입이 완료되었습니다.");
  }

  @PostMapping("/signin")
  ResponseEntity<?> signIn(SignInForm signInForm) {

    return ResponseEntity.ok(userService.signIn(signInForm));
  }


}
