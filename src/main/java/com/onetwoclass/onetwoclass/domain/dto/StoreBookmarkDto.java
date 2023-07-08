package com.onetwoclass.onetwoclass.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreBookmarkDto {

  private Long id;

  private Long storeId;

  private String storeName;

}
