package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetwoclass.onetwoclass.config.redis.RedisConfig;
import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.DayClassBookmarkDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassBookmark;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.AddDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.DeleteDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassBookmarkRepository;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class DayClassBookmarkServiceTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DayClassRepository dayClassRepository;

  @Autowired
  private DayClassBookmarkService dayClassBookmarkService;

  @Autowired
  private DayClassBookmarkRepository dayClassBookmarkRepository;

  @Test
  @DisplayName("데이클래스 북마크 추가 성공")
  void success_addDayClassBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_addDayClassBookmark@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    AddDayClassBookmarkForm addDayClassBookmarkForm =
        AddDayClassBookmarkForm.builder().dayClassId(dayClass.getId()).build();

    //when
    dayClassBookmarkService.addDayClassBookmark(addDayClassBookmarkForm, customer);

    Page<DayClassBookmark> dayClassBookmarkList =
        dayClassBookmarkRepository.findAllByCustomerId(customer.getId(), Pageable.unpaged());

    //then
    assertEquals(dayClassBookmarkList.getTotalElements(), 1);

  }

  @Test
  @DisplayName("데이클래스 북마크 추가 실패 - 이미 등록된 북마크")
  void fail_addDayClassBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("fail_addDayClassBookmark@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    AddDayClassBookmarkForm addDayClassBookmarkForm =
        AddDayClassBookmarkForm.builder().dayClassId(dayClass.getId()).build();

    //when
    dayClassBookmarkService.addDayClassBookmark(addDayClassBookmarkForm, customer);

    CustomException customException = assertThrows(CustomException.class, () ->
        dayClassBookmarkService.addDayClassBookmark(addDayClassBookmarkForm, customer));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_DAYCLASS_BOOKMARK);

  }

  @Test
  @DisplayName("데이클래스 북마크 삭제 성공")
  void success_deleteDayClassBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_deleteDayClassBookmark@test.com")
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

    DayClass dayClass1 = dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClass dayClass2 = dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClassBookmark dayClassBookmark = dayClassBookmarkRepository.save(DayClassBookmark.builder()
        .customer(customer)
        .dayClass(dayClass1)
        .build());

    dayClassBookmarkRepository.save(DayClassBookmark.builder()
        .customer(customer)
        .dayClass(dayClass2)
        .build());

    DeleteDayClassBookmarkForm deleteDayClassBookmarkForm =
        DeleteDayClassBookmarkForm.builder().dayClassBookmarkId(dayClassBookmark.getId()).build();

    //when
    Page<DayClassBookmark> dayClassBookmarkList1 =
        dayClassBookmarkRepository.findAllByCustomerId(customer.getId(), Pageable.unpaged());

    dayClassBookmarkService.deleteDayClassBookmark(deleteDayClassBookmarkForm, customer);

    Page<DayClassBookmark> dayClassBookmarkList2 =
        dayClassBookmarkRepository.findAllByCustomerId(customer.getId(), Pageable.unpaged());

    //then
    assertEquals(dayClassBookmarkList1.getTotalElements(), 2);
    assertEquals(dayClassBookmarkList2.getTotalElements(), 1);

  }

  @Test
  @DisplayName("데이클래스 북마크 가져오기 성공")
  void success_getDayClassBookmark() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_getDayClassBookmark@test.com")
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

    DayClass dayClass1 = dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClass dayClass2 = dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    dayClassBookmarkRepository.save(DayClassBookmark.builder()
        .customer(customer)
        .dayClass(dayClass1)
        .build());

    dayClassBookmarkRepository.save(DayClassBookmark.builder()
        .customer(customer)
        .dayClass(dayClass2)
        .build());

    //when
    Page<DayClassBookmarkDto> dayClassBookmarkDtoList =
        dayClassBookmarkService.getDayClassBookmark(customer, Pageable.unpaged());

    //then
    assertEquals(dayClassBookmarkDtoList.getTotalElements(), 2);

  }


}