package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetwoclass.onetwoclass.config.elasticsearch.ElasticTestContainer;
import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.DayClassSchedulerDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclassscheduler.AddDayClassSchedulerForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@Import(ElasticTestContainer.class)
class DayClassSchedulerServiceTest {

  @Autowired
  private DayClassSchedulerService dayClassSchedulerService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DayClassSearchRepository dayClassSearchRepository;

  @Autowired
  private DayClassSchedulerRepository dayClassSchedulerRepository;

  @Test
  @DisplayName("데이클래스 스케쥴러 추가 성공")
  void success_addDayClassScheduler() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_addDayClassScheduler@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassName("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    AddDayClassSchedulerForm addDayClassSchedulerForm =
        AddDayClassSchedulerForm.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.of(2023, 7, 20, 15, 0, 0))
            .build();

    //when
    dayClassSchedulerService.addDayClassScheduler(addDayClassSchedulerForm, seller);

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.findAllByDayClassId(dayClassDocument.getId(),
                Pageable.unpaged())
            .getContent().get(0);

    //then
    assertEquals(dayClassScheduler.getDayClassId(), addDayClassSchedulerForm.getDayClassId());
    assertEquals(dayClassScheduler.getScheduledDate(), addDayClassSchedulerForm.getScheduledDate());

  }

  @Test
  @DisplayName("데이클래스 스케쥴러 추가 실패 - 데이클래스와 셀러 다름")
  void fail_addDayClassScheduler_MismatchSellerAndDayClass() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_addDayClassScheduler_MismatchSellerAndDayClass@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member seller2 = memberRepository.save(Member.builder()
        .email("fail_addDayClassScheduler_MismatchSellerAndDayClass2@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller2)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassName("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    AddDayClassSchedulerForm addDayClassSchedulerForm =
        AddDayClassSchedulerForm.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.of(2023, 7, 20, 15, 0, 0))
            .build();

    //when
    CustomException customException =
        assertThrows(CustomException.class,
            () -> dayClassSchedulerService.addDayClassScheduler(addDayClassSchedulerForm,
                seller2));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);

  }

  @Test
  @DisplayName("데이클래스 스케쥴러 추가 실패 - 시케쥴러 중복")
  void fail_addDayClassScheduler_AlreadyExistDayClassScheduler() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_addDayClassScheduler_AlreadyExistDayClassScheduler@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassName("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    AddDayClassSchedulerForm addDayClassSchedulerForm =
        AddDayClassSchedulerForm.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.of(2023, 7, 20, 15, 0, 0))
            .build();

    //when
    dayClassSchedulerService.addDayClassScheduler(addDayClassSchedulerForm, seller);

    CustomException customException =
        assertThrows(CustomException.class,
            () -> dayClassSchedulerService.addDayClassScheduler(addDayClassSchedulerForm,
                seller));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_DAYCLASS_SCHEDULER);

  }

  @Test
  @DisplayName("데이클래스 스케쥴러 클래스 id 와 email 로 정보 가져오기 성공")
  void success_getDayClassSchedulerByDayClassIdAndEmail() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_getDayClassSchedulerByDayClassIdAndEmail@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassName("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .scheduledDate(LocalDateTime.of(2023, 7, 20, 15, 0, 0))
        .dayClassId(dayClassDocument.getId())
        .build());

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .scheduledDate(LocalDateTime.of(2023, 7, 21, 15, 0, 0))
        .dayClassId(dayClassDocument.getId())
        .build());

    //when
    Page<DayClassSchedulerDto> dayClassSchedulerDtoList =
        dayClassSchedulerService.getDayClassSchedulerByDayClassIdAndEmail(
            dayClassDocument.getId(), seller, Pageable.unpaged());

    //then
    assertEquals(dayClassSchedulerDtoList.getTotalElements(), 2);
  }

  @Test
  @DisplayName("데이클래스 스케쥴러 정보 데이클래스 id로 가져오기")
  void success_getDayClassSchedulerByDayClassId() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_getDayClassSchedulerByDayClassId@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassName("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .scheduledDate(LocalDateTime.of(2023, 7, 20, 15, 0, 0))
        .dayClassId(dayClassDocument.getId())
        .build());

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .scheduledDate(LocalDateTime.of(2023, 7, 21, 15, 0, 0))
        .dayClassId(dayClassDocument.getId())
        .build());

    //when
    Page<DayClassSchedulerDto> dayClassSchedulerDtoList =
        dayClassSchedulerService.getDayClassSchedulerByDayClassId(
            dayClassDocument.getId(), Pageable.unpaged());

    //then
    assertEquals(dayClassSchedulerDtoList.getTotalElements(), 2);
  }


}