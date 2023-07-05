package com.onetwoclass.onetwoclass.domain.form.dayclass;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddDayClassForm {

  @NotNull(message = "데이클래스명을 기재해주세요.")
  private String dayClassName;

  @NotNull(message = "데이클래스 설명을 기재해주세요.")
  private String explains;

  @Min(value = 0, message = "가격을 확인해주세요.")
  @NotNull(message = "가격을 기재해주세요.")
  private Integer price;

}
