package com.onetwoclass.onetwoclass.domain.form.dayclassscheduler;

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
public class AddDayClassSchedulerForm {

  @NotNull(message = "데이클래스의 id를 기재해주세요.")
  private String dayClassId;

  @NotNull(message = "등록하시고자 하는 날짜를 기재해주세요.")
  private LocalDateTime scheduledDate;

}
