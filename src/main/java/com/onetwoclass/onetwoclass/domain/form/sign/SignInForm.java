package com.onetwoclass.onetwoclass.domain.form.sign;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm {

  @Email
  private String email;

  private String password;

}
