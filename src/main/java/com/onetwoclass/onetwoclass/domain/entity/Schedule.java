package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.ScheduleDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean acceptYn;

  private LocalDateTime requestedDateTime;

  @CreatedDate
  private LocalDateTime registeredAt;

  @ManyToOne
  @JoinColumn(name = "day_class_scheduler_id")
  private DayClassScheduler dayClassScheduler;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customer;

  public void acceptRequestSchedule(){
    acceptYn = true;
  }

  public static ScheduleDto toScheduleDto(Schedule schedule) {
    return ScheduleDto.builder()
        .scheduleId(schedule.id)
        .customerId(schedule.customer.getId())
        .registeredAt(schedule.registeredAt)
        .acceptYn(schedule.acceptYn)
        .dayClassSchedulerId(schedule.dayClassScheduler.getId())
        .build();
  }

}
