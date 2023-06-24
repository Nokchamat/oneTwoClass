package com.onetwoclass.onetwoclass.domain.form;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class SignUpForm {

  @Email
  private String email;

  @Length(min = 1, max = 30, message = "이름을 확인해주세요.")
  private String name;

  @Length(min = 8, max = 100, message = "비밀번호는 보안을 위해 최소 8자리 입니다.")
  private String password;

  @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$"
      , message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxxx-xxxx")
  private String phone;

  @Max(value = 130, message = "나이 정보를 확인해주세요.")
  @Min(value = 8, message = "최소 나이는 8세 입니다.")
  private Integer age;

  private Role role;

}
