package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.ChattingRoomDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChattingRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String storeName;

  private Boolean exitSellerYn;

  private Boolean exitCustomerYn;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customer;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private Member seller;

  @CreatedDate
  private LocalDateTime registeredAt;

  public void setExitCustomerYn() {
    this.exitCustomerYn = true;
  }

  public void setExitSellerYn() {
    this.exitSellerYn = true;
  }

  public boolean isEmptyRoom() {

    if (!exitSellerYn) {
      return false;
    }

    if (!exitCustomerYn) {
      return false;
    }

    return true;
  }

  public static ChattingRoomDto toChattingRoomDto(ChattingRoom chattingRoom) {
    return ChattingRoomDto.builder()
        .chattingRoomId(chattingRoom.getId())
        .storeName(chattingRoom.getStoreName())
        .sellerId(chattingRoom.getSeller().getId())
        .customerId(chattingRoom.getCustomer().getId())
        .exitCustomerYn(chattingRoom.getExitCustomerYn())
        .exitSellerYn(chattingRoom.getExitSellerYn())
        .build();
  }

}
