package com.onetwoclass.onetwoclass.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeDto {

  private Long id;

  private String subject;

  private String text;

}
