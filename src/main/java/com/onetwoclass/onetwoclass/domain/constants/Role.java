package com.onetwoclass.onetwoclass.domain.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  CUSTOMER("ROLE_CUSTOMER"),
  SELLER("ROLE_SELLER");

  private final String detail;

}
