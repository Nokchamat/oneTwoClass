package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.StoreBookmarkDto;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.entity.StoreBookmark;
import com.onetwoclass.onetwoclass.domain.form.storebookmark.AddStoreBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.storebookmark.DeleteStoreBookmarkForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreBookmarkRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreBookmarkService {

  private final StoreBookmarkRepository storeBookmarkRepository;

  private final StoreRepository storeRepository;

  public void addStoreBookmark(AddStoreBookmarkForm addStoreBookmarkForm, Member customer) {

    Store store = storeRepository.findById(addStoreBookmarkForm.getStoreId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    storeBookmarkRepository.findByCustomerIdAndStoreId(customer.getId(), store.getId())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_STORE_BOOKMARK);
        });

    storeBookmarkRepository.save(StoreBookmark.builder()
        .store(store)
        .customer(customer)
        .build());

  }

  public void deleteStoreBookmark(DeleteStoreBookmarkForm deleteStoreBookmarkForm,
      Member customer) {

    StoreBookmark storeBookmark =
        storeBookmarkRepository.findById(deleteStoreBookmarkForm.getStoreBookmarkId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE_BOOKMARK));

    if (storeBookmark.getCustomer().getId() != customer.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_STORE_BOOKMARK);
    }

    storeBookmarkRepository.delete(storeBookmark);

  }

  public List<StoreBookmarkDto> getStoreBookmark(Member customer, Pageable pageable) {

    return storeBookmarkRepository.findAllByCustomerId(customer.getId(), pageable)
        .stream().map(StoreBookmark::toStoreBookmarkDto).collect(Collectors.toList());
  }

}
