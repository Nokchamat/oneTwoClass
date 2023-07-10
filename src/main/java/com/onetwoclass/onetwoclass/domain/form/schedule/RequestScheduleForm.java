package com.onetwoclass.onetwoclass.domain.form.schedule;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestScheduleForm {

  @NotNull(message = "데이클래스 스케쥴러 id를 기재해주세요.")
  private Long dayClassSchedulerId;

}
