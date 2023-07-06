package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.StoreBookmarkDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StoreBookmark {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customer;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  public static StoreBookmarkDto toStoreBookmarkDto(StoreBookmark storeBookmark) {
    return StoreBookmarkDto.builder()
        .id(storeBookmark.id)
        .storeId(storeBookmark.getStore().getId())
        .storeName(storeBookmark.getStore().getStoreName())
        .build();
  }

}
