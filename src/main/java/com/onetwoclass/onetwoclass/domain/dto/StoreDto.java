package com.onetwoclass.onetwoclass.domain.dto;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StoreDto {

  private Long storeId;

  private String storeName;

  private String explains;

  private Category category;

}
