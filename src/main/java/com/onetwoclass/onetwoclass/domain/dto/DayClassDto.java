package com.onetwoclass.onetwoclass.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DayClassDto {

  private String dayClassName;

  private String explains;

  private Integer price;

  private LocalDateTime registeredAt;

  private LocalDateTime modifiedAt;

}
