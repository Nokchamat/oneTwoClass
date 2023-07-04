package com.onetwoclass.onetwoclass.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean acceptYn;

  private LocalDateTime requestedDateTime;

  @ManyToOne
  @JoinColumn(name = "day_class_scheduler")
  private DayClassScheduler dayClassScheduler;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customerId;

}
