package com.onetwoclass.onetwoclass.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DayClassSchedulerDto {

  private String dayClassName;

  private LocalDateTime scheduledDate;

}
