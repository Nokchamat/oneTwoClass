package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onetwoclass.onetwoclass.config.ContainerExtension;
import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.DayClassDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclass.AddDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.DeleteDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

@SpringBootTest
@ExtendWith(ContainerExtension.class)
class DayClassServiceTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DayClassSearchRepository dayClassSearchRepository;

  @Autowired
  private DayClassService dayClassService;

  @Test
  @DisplayName("데이클래스 추가 성공")
  void success_addDayClass() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_addDayClass@test.com")
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

    AddDayClassForm addDayClassForm =
        AddDayClassForm.builder()
            .dayClassName("마카롱 클래스!!!!!!!")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .build();

    //when
    dayClassService.addDayClass(addDayClassForm, seller);

    DayClassDocument dayClassDocument =
        dayClassSearchRepository.findAllByStoreId(store.getId()).get(0);

    //then
    assertEquals(dayClassDocument.getExplains(), addDayClassForm.getExplains());
    assertEquals(dayClassDocument.getDayClassNameKeyword(), addDayClassForm.getDayClassName());
    assertEquals(dayClassDocument.getPrice(), addDayClassForm.getPrice());

  }

  @Test
  @DisplayName("데이클래스 추가 실패 - 데이클래스 이름 중복")
  void fail_addDayClass_DuplicationDayClassName() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_addDayClass_DuplicationDayclassName@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    AddDayClassForm addDayClassForm =
        AddDayClassForm.builder()
            .dayClassName("마카롱 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .build();

    //when
    dayClassService.addDayClass(addDayClassForm, seller);

    CustomException customException = assertThrows(CustomException.class,
        () -> dayClassService.addDayClass(addDayClassForm, seller));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.DUPLICATION_DAYCLASS_NAME);

  }

  @Test
  @DisplayName("데이클래스 업데이트 성공")
  void success_updateDayClass() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_updateDayClass@test.com")
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

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameKeyword("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    dayClassSearchRepository.save(DayClassDocument.builder()
        .id(dayClassDocument.getId())
        .dayClassNameText("마카롱 클래스")
        .dayClassNameKeyword("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    UpdateDayClassForm updateDayClassForm = UpdateDayClassForm.builder()
        .dayClassId(dayClassDocument.getId())
        .toChangeDayClassName("빵 클래스")
        .toChangeExplains("냄새도 좋은 빵")
        .toChangePrice(2000)
        .build();

    //when
    dayClassService.updateDayClass(updateDayClassForm, seller);

    System.out.println(dayClassDocument.getId());

    DayClassDocument updatedDayClass = dayClassSearchRepository.findById(dayClassDocument.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    //then
    assertEquals(updatedDayClass.getDayClassNameKeyword(),
        updateDayClassForm.getToChangeDayClassName());
    assertEquals(updatedDayClass.getExplains(), updateDayClassForm.getToChangeExplains());
    assertEquals(updatedDayClass.getPrice(), updateDayClassForm.getToChangePrice());

  }

  @Test
  @DisplayName("데이클래스 업데이트 실패 - 데이클래스 이름 중복")
  void fail_updateDayClass_DuplicationDayClassName() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_updateDayClass_DuplicationDayClassName@test.com")
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

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    UpdateDayClassForm updateDayClassForm = UpdateDayClassForm.builder()
        .dayClassId(dayClassDocument.getId())
        .toChangeDayClassName("마카롱 클래스")
        .toChangeExplains("냄새도 좋은 빵")
        .toChangePrice(2000)
        .build();

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> dayClassService.updateDayClass(updateDayClassForm, seller));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.DUPLICATION_DAYCLASS_NAME);

  }

  @Test
  @DisplayName("데이클래스 업데이트 실패 - 업데이트 대상 데이클래스 없음")
  void fail_updateDayClass_NotFoundDayClass() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_updateDayClass_NotFoundDayClass@test.com")
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

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    UpdateDayClassForm updateDayClassForm = UpdateDayClassForm.builder()
        .dayClassId(dayClassDocument.getId() + "djsdfkdsfl")
        .toChangeDayClassName("찐빵 클래스")
        .toChangeExplains("냄새도 좋은 빵")
        .toChangePrice(2000)
        .build();

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> dayClassService.updateDayClass(updateDayClassForm, seller));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_DAYCLASS);

  }

  @Test
  @DisplayName("데이클래스 가져오기 성공")
  void success_getDayClassByEmail() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_getDayClassByEmail@test.com")
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

    DayClassDocument dayClassDocument1 = dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    DayClassDocument dayClassDocument2 = dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("빵 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    //when
    Page<DayClassDto> dayClassDtoList =
        dayClassService.getDayClassBySeller(seller, Pageable.unpaged());

    //then
    assertEquals(dayClassDtoList.getTotalElements(), 2);
  }

  @Test
  @DisplayName("데이클래스 삭제 성공")
  void success_deleteDayClassByEmail() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_deleteDayClassByEmail@test.com")
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

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .storeId(store.getId())
        .build());

    DeleteDayClassForm deleteDayClassForm = DeleteDayClassForm.builder()
        .dayClassId(dayClassDocument.getId()).build();

    //when
    dayClassService.deleteDayClass(deleteDayClassForm, seller);

    SearchHits<DayClassDocument> dayClassDocumentSearchHits =
        dayClassService.findByStoreIdAndDayClassNameKeyword(store.getId(), "마카롱 클래스");

    //then
    assertTrue(dayClassDocumentSearchHits.isEmpty());
  }

  @Test
  @DisplayName("데이클래스 전부 가져오기 성공")
  void success_getAllDayClass() {
    //given
    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .build());

    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("마카롱 클래스")
        .dayClassNameText("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .build());

    //when
    Page<DayClassDto> dayClassDtoList =
        dayClassService.getAllDayClassFromElasticsearch(Pageable.ofSize(2));

    //then
    assertEquals(dayClassDtoList.getContent().size(), 2);

  }

  @Test
  @DisplayName("데이클래스 이름으로 전부 가져오기 성공")
  void success_getAllDayClassByDayClassName() {
    //given
    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameText("데이클래스 가져오기 테스트1")
        .dayClassNameKeyword("데이클래스 가져오기 테스트1")
        .explains("success_getAllDayClassByDayClassName")
        .price(5000)
        .build());

    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameKeyword("데이클래스 가져오기 테스트2")
        .dayClassNameText("데이클래스 가져오기 테스트2")
        .explains("success_getAllDayClassByDayClassName")
        .price(5000)
        .build());

    //when
    Page<DayClassDto> dayClassDtoList =
        dayClassService.getAllDayClassByDayClassNameFromElasticsearch(
            "데이클래스 가져오기 테스트", Pageable.unpaged());

    //then
    assertEquals(dayClassDtoList.getTotalElements(), 2);

    assertEquals(dayClassDtoList.getContent().get(0).getExplains(),
        "success_getAllDayClassByDayClassName");
    assertEquals(dayClassDtoList.getContent().get(1).getExplains(),
        "success_getAllDayClassByDayClassName");

  }

  @Test
  @DisplayName("데이클래스 스토어ID로 전부 가져오기 성공")
  void success_getAllDayClassByStoreId() {
    //given

    Store store = storeRepository.save(Store.builder()
        .explains("")
        .category(Category.DESSERT)
        .build());

    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameText("success_getAllDayClassByStoreId1")
        .dayClassNameKeyword("success_getAllDayClassByStoreId1")
        .explains("success_getAllDayClassByStoreId")
        .price(5000)
        .storeId(store.getId())
        .build());

    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameText("success_getAllDayClassByStoreId2")
        .dayClassNameKeyword("success_getAllDayClassByStoreId2")
        .explains("success_getAllDayClassByStoreId")
        .price(5000)
        .storeId(store.getId())
        .build());

    //when
    Page<DayClassDto> dayClassDtoList =
        dayClassService.getAllDayClassByStoreId(
            store.getId(), Pageable.ofSize(5));

    //then
    assertEquals(dayClassDtoList.getTotalElements(), 2);
    assertEquals(dayClassDtoList.getContent().get(0).getExplains(),
        "success_getAllDayClassByStoreId");
    assertEquals(dayClassDtoList.getContent().get(1).getExplains(),
        "success_getAllDayClassByStoreId");

  }


}