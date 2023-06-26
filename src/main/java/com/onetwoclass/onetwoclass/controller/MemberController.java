package com.onetwoclass.onetwoclass.controller;

import com.onetwoclass.onetwoclass.domain.form.SignInForm;
import com.onetwoclass.onetwoclass.domain.form.SignUpForm;
import com.onetwoclass.onetwoclass.exception.CustomValidationException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

  private final MemberService userService;

  @PostMapping("/signup")
  ResponseEntity<?> signUp(@RequestBody @Validated SignUpForm signUpForm,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      throw new CustomValidationException(ErrorCode.PLEASE_CHECK_INFORMATION,
          bindingResult.getAllErrors());
    }

    userService.signUp(signUpForm);

    return ResponseEntity.ok(signUpForm.getName() + "님의 회원가입이 완료되었습니다.");
  }


  @PostMapping("/signin")
  ResponseEntity<?> signIn(@RequestBody @Validated SignInForm signInForm) {

    return ResponseEntity.ok(userService.signIn(signInForm));
  }

}
