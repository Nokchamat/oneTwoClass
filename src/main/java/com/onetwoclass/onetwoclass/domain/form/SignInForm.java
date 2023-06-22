package com.onetwoclass.onetwoclass.domain.form;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignInForm {

  @Email
  private String email;

  private String password;

}
