package com.onetwoclass.onetwoclass.domain.form.dayclassbookmark;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddDayClassBookmarkForm {

  @NotNull(message = "추가할 데이클래스 아이디를 기재해주세요.")
  private String dayClassId;

}
