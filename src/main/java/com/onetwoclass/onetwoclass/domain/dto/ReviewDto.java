package com.onetwoclass.onetwoclass.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDto {

  private Long id;

  private String text;

  private Integer star;

  private LocalDateTime registeredAt;

  private Long dayClassId;

  private String dayClassName;

  private Long scheduleId;

}
