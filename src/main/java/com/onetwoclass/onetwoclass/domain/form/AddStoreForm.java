package com.onetwoclass.onetwoclass.domain.form;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddStoreForm {

  private String storename;

  private String explains;

  private Category category;

}
