package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.StoreDto;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.store.AddStoreForm;
import com.onetwoclass.onetwoclass.domain.form.store.UpdateStoreForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  private final MemberRepository memberRepository;

  private final DayClassRepository dayClassRepository;

  @Transactional
  public void addStore(AddStoreForm addStoreForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    storeRepository.findBySellerId(seller.getId())
        .ifPresent(
            s -> {
              throw new CustomException(ErrorCode.ALREADY_EXIST_STORE);
            });

    storeRepository.save(Store.builder()
        .storeName(addStoreForm.getStorename())
        .explains(addStoreForm.getExplains())
        .category(addStoreForm.getCategory())
        .seller(seller)
        .build());
  }

  @Transactional
  public void updateStore(UpdateStoreForm updateStoreForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    store.updateStore(updateStoreForm);

  }

  public StoreDto getStore(String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    return Store.toStoreDto(
        storeRepository.findBySellerId(seller.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE)));
  }

  public void deleteStore(String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    if (!dayClassRepository.findByStoreId(store.getId()).isEmpty()) {
      throw new CustomException(ErrorCode.PLEASE_DELETE_DAYCLASS_FIRST);
    }

    storeRepository.delete(store);
  }

}
