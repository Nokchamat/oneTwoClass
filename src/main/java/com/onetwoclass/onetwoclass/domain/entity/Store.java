package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.dto.StoreDto;
import com.onetwoclass.onetwoclass.domain.form.store.UpdateStoreForm;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Store extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String storeName;

  @Lob
  private String explains;

  @Enumerated(EnumType.STRING)
  private Category category;

  @OneToOne
  @JoinColumn(name = "seller_id")
  private Member seller;

  public void updateStore(UpdateStoreForm updateStoreForm) {

    if (updateStoreForm.getStorename() != null) {
      this.storeName = updateStoreForm.getStorename();
    }

    if (updateStoreForm.getExplains() != null) {
      this.explains = updateStoreForm.getExplains();
    }

    if (updateStoreForm.getCategory() != null) {
      this.category = updateStoreForm.getCategory();
    }

  }

  public static StoreDto toStoreDto(Store store) {
    return StoreDto.builder()
        .storeName(store.storeName)
        .explains(store.explains)
        .category(store.category)
        .build();
  }

}
