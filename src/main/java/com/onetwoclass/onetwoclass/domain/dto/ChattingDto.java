package com.onetwoclass.onetwoclass.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChattingDto {

  private Long postMemberId;

  private String text;

  private LocalDateTime registeredAt;

}
