package com.onetwoclass.onetwoclass.domain.form;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignInForm {

  private String email;

  private String password;

}
