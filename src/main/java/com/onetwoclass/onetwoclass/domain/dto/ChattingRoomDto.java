package com.onetwoclass.onetwoclass.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChattingRoomDto {

  private Long chattingRoomId;

  private String storeName;

  private Long customerId;

  private Long sellerId;

  private Boolean exitSellerYn;

  private Boolean exitCustomerYn;

}
