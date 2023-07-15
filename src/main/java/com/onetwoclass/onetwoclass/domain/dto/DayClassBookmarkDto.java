package com.onetwoclass.onetwoclass.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DayClassBookmarkDto {

  private Long id;

  private String dayClassId;

  private String dayClassName;

}
