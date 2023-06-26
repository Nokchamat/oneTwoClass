package com.onetwoclass.onetwoclass.domain.form;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddStoreForm {

  @NotNull
  private String storename;

  @NotNull
  private String explains;

  @NotNull
  private Category category;

}
