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
public class AddNoticeForm {

  @NotNull(message = "제목을 기재해주세요.")
  private String subject;

  @NotNull(message = "내용을 기재해주세요.")
  private String text;

}
