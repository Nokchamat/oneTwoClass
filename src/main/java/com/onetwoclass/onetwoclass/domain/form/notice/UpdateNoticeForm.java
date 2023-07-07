package com.onetwoclass.onetwoclass.domain.form.notice;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UpdateNoticeForm {

  @NotNull(message = "업데이트 할 공지사항 아이디를 기재해주세요.")
  private Long noticeId;

  private String subject;

  private String text;

}
