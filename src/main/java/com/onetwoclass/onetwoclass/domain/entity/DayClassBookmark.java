package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.DayClassBookmarkDto;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DayClassBookmark {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customer;

  private String dayClassId;

  public static DayClassBookmarkDto toDayClassBookmarkDto(DayClassBookmark dayClassBookmark) {
    return DayClassBookmarkDto.builder()
        .id(dayClassBookmark.getId())
        .dayClassId(dayClassBookmark.dayClassId)
        .build();
  }

}
