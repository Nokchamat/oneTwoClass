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
public class DeleteNoticeForm {

  @NotNull(message = "삭제할 공지사항 id를 기재해주세요.")
  private Long noticeId;

}
