package com.onetwoclass.onetwoclass.domain.form.store;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStoreForm {

  private String storename;

  private String explains;

  private Category category;

}
