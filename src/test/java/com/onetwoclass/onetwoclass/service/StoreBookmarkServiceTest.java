package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class StoreBookmarkServiceTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private StoreBookmarkRepository storeBookmarkRepository;

  @Autowired
  private StoreBookmarkService storeBookmarkService;

  @Test
  @DisplayName("스토어 북마크 추가 성공")
  void success_addStoreBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_addStoreBookmark@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(customer)
        .build());

    AddStoreBookmarkForm addStoreBookmarkForm = AddStoreBookmarkForm.builder()
        .storeId(store.getId())
        .build();
    //when
    storeBookmarkService.addStoreBookmark(addStoreBookmarkForm, customer.getEmail());

    StoreBookmark storeBookmark = storeBookmarkRepository
        .findByCustomerIdAndStoreId(customer.getId(), addStoreBookmarkForm.getStoreId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE_BOOKMARK));
    //then
    assertEquals(storeBookmark.getCustomer().getEmail(), customer.getEmail());
    assertEquals(storeBookmark.getStore().getStoreName(), store.getStoreName());

  }

  @Test
  @DisplayName("스토어 북마크 추가 실패 - 중복 추가")
  void fail_addStoreBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("fail_addStoreBookmark@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(customer)
        .build());

    AddStoreBookmarkForm addStoreBookmarkForm = AddStoreBookmarkForm.builder()
        .storeId(store.getId())
        .build();
    //when
    storeBookmarkService.addStoreBookmark(addStoreBookmarkForm, customer.getEmail());

    CustomException customException = assertThrows(CustomException.class,
        () -> storeBookmarkService.addStoreBookmark(addStoreBookmarkForm, customer.getEmail()));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_STORE_BOOKMARK);

  }

  @Test
  @DisplayName("스토어 북마크 삭제 성공")
  void success_deleteStoreBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_deleteStoreBookmark@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(customer)
        .build());

    Store store2 = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(customer)
        .build());

    StoreBookmark storeBookmark = storeBookmarkRepository.save(StoreBookmark.builder()
        .customer(customer)
        .store(store)
        .build());

    StoreBookmark storeBookmark2 = storeBookmarkRepository.save(StoreBookmark.builder()
        .customer(customer)
        .store(store2)
        .build());

    DeleteStoreBookmarkForm deleteStoreBookmarkForm =
        DeleteStoreBookmarkForm.builder()
            .storeBookmarkId(storeBookmark.getId())
            .build();

    storeBookmarkService.deleteStoreBookmark(deleteStoreBookmarkForm, customer.getEmail());

    //when
    List<StoreBookmark> storeBookmarkList =
        storeBookmarkRepository.findAllByCustomerId(customer.getId(), Pageable.unpaged());

    //then
    assertEquals(storeBookmarkList.size(), 1);

  }

  @Test
  @DisplayName("스토어 북마크 가져오기 성공")
  void success_getStoreBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_getStoreBookmark@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(customer)
        .build());

    Store store2 = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(customer)
        .build());

    StoreBookmark storeBookmark = storeBookmarkRepository.save(StoreBookmark.builder()
        .customer(customer)
        .store(store)
        .build());

    StoreBookmark storeBookmark2 = storeBookmarkRepository.save(StoreBookmark.builder()
        .customer(customer)
        .store(store2)
        .build());

    //when
    List<StoreBookmarkDto> storeBookmarkDtoList =
        storeBookmarkService.getStoreBookmark(customer.getEmail(), Pageable.unpaged());

    //then
    assertEquals(storeBookmarkDtoList.size(), 2);

  }


}