package com.onetwoclass.onetwoclass.domain.form.dayclass;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDayClassForm {

  @NotNull(message = "변경할 DayClassId를 기재해주세요.")
  private String dayClassId;

  @Size(min = 1, max = 20)
  private String toChangeDayClassName;

  private String toChangeExplains;

  @Min(0)
  private Integer toChangePrice;

}
