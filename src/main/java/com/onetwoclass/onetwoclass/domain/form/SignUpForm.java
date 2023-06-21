package com.onetwoclass.onetwoclass.domain.form;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpForm {

  private String email;

  private String name;

  private String password;

  private String phone;

  private Role role;

}
