package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.ReviewDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  private String text;

  private Integer star;

  @CreatedDate
  private LocalDateTime registeredAt;

  @ManyToOne
  @JoinColumn(name = "day_class_id")
  private DayClass dayClass;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customer;

  @OneToOne
  @JoinColumn(name = "schedule_id")
  private Schedule schedule;

  public static ReviewDto toReviewDto(Review review) {
    return ReviewDto.builder()
        .id(review.getId())
        .text(review.getText())
        .star(review.getStar())
        .registeredAt(review.getRegisteredAt())
        .dayClassId(review.getDayClass().getId())
        .dayClassName(review.getDayClass().getDayClassName())
        .scheduleId(review.getSchedule().getId())
        .build();
  }

}
