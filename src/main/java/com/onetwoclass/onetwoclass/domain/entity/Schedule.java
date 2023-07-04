package com.onetwoclass.onetwoclass.domain.entity;

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
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
  @JoinColumn(name = "day_class_scheduler")
  private DayClassScheduler dayClassScheduler;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customerId;

}
