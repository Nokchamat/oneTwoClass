package com.onetwoclass.onetwoclass.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DayClassBookmarkDto {

  private Long id;

  private Long dayClassId;

  private String dayClassName;

}
