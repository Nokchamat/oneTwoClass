package com.onetwoclass.onetwoclass.domain.form.dayclass;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateDayClassForm {

  @NotNull(message = "변경 대상의 이름을 기재해주세요.")
  private String dayClassName;

  @Size(min = 1, max = 20)
  private String toChangeDayClassName;

  private String toChangeExplains;

  @Min(0)
  private Integer toChangePrice;

}
