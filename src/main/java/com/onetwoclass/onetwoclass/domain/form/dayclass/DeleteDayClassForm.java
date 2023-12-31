package com.onetwoclass.onetwoclass.domain.form.dayclass;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteDayClassForm {

  @NotNull(message = "삭제하실 데이클래스의 아이디를 기재해주세요.")
  private String dayClassId;

}
