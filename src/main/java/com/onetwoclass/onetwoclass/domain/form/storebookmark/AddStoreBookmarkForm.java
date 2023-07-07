package com.onetwoclass.onetwoclass.domain.form.storebookmark;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddStoreBookmarkForm {

  @NotNull(message = "추가할 스토어의 아이디를 기재해주세요.")
  private Long storeId;

}
