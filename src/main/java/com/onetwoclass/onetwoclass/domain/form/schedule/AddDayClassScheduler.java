package com.onetwoclass.onetwoclass.domain.form.schedule;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AddDayClassScheduler {

  @NotNull(message = "데이클래스명을 기재해주세요.")
  private String dayClassName;

  @NotNull(message = "등록하시고자 하는 날짜를 기재해주세요.")
  private LocalDateTime scheduledDate;

}
