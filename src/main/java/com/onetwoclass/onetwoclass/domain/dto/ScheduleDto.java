package com.onetwoclass.onetwoclass.domain.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {

  private Long scheduleId;

  private Boolean acceptYn;

  private LocalDateTime registeredAt;

  private Long dayClassSchedulerId;

  private Long customerId;

}
