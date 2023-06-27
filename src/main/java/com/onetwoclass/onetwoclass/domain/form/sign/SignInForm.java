package com.onetwoclass.onetwoclass.domain.form.sign;

import javax.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignInForm {

  @Email
  private String email;

  private String password;

}