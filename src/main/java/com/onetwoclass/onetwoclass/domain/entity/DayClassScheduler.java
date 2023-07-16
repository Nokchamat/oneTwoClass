package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.DayClassSchedulerDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DayClassScheduler {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String dayClassId;

  private LocalDateTime scheduledDate;

  public static DayClassSchedulerDto toDayClassSchedulerDto(DayClassScheduler dayClassScheduler) {
    return DayClassSchedulerDto.builder()
        .dayClassSchedulerId(dayClassScheduler.id)
        .scheduledDate(dayClassScheduler.scheduledDate)
        .build();
  }

}
