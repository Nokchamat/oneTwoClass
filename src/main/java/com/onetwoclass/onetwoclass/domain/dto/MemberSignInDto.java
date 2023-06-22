package com.onetwoclass.onetwoclass.domain.dto;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignInDto {

  private String email;

  private String password;

  private Role role;

}
