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
public class DeleteStoreBookmarkForm {

  @NotNull(message = "삭제하실 스토어 북마크 아이디를 기재해주세요.")
  private Long storeBookmarkId;

}
