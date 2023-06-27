package com.onetwoclass.onetwoclass.domain.form.store;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddStoreForm {

  @NotNull(message = "가게 이름을 기재해주세요.")
  @Size(min = 1, max = 15, message = "가게 이름은 최소 1글자, 최대 15글자 입니다.")
  private String storename;

  @NotNull(message = "가게 설명을 기재해주세요.")
  private String explains;

  @NotNull(message = "가게 카테고리를 기재해주세요.")
  private Category category;

}
