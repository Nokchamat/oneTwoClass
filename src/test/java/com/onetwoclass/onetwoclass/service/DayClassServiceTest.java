package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.DayClassDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclass.AddDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.DeleteDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class DayClassServiceTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DayClassRepository dayClassRepository;

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
            .dayClassName("마카롱 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .build();

    //when
    dayClassService.addDayClass(addDayClassForm, seller.getEmail());

    DayClass dayClass = dayClassRepository.findAllByStoreId(store.getId()).get(0);

    //then
    assertEquals(dayClass.getDayClassName(), addDayClassForm.getDayClassName());
    assertEquals(dayClass.getExplains(), addDayClassForm.getExplains());
    assertEquals(dayClass.getPrice(), addDayClassForm.getPrice());

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
    dayClassService.addDayClass(addDayClassForm, seller.getEmail());

    CustomException customException = assertThrows(CustomException.class,
        () -> dayClassService.addDayClass(addDayClassForm, seller.getEmail()));

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

    dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    UpdateDayClassForm updateDayClassForm = UpdateDayClassForm.builder()
        .dayClassName("마카롱 클래스")
        .toChangeDayClassName("빵 클래스")
        .toChangeExplains("냄새도 좋은 빵")
        .toChangePrice(2000)
        .build();

    //when
    dayClassService.updateDayClass(updateDayClassForm, seller.getEmail());

    DayClass dayClass = dayClassRepository.findAllByStoreId(store.getId()).get(0);

    //then
    assertEquals(dayClass.getDayClassName(), updateDayClassForm.getToChangeDayClassName());
    assertEquals(dayClass.getExplains(), updateDayClassForm.getToChangeExplains());
    assertEquals(dayClass.getPrice(), updateDayClassForm.getToChangePrice());

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

    dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    UpdateDayClassForm updateDayClassForm = UpdateDayClassForm.builder()
        .dayClassName("마카롱 클래스")
        .toChangeDayClassName("마카롱 클래스")
        .toChangeExplains("냄새도 좋은 빵")
        .toChangePrice(2000)
        .build();

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> dayClassService.updateDayClass(updateDayClassForm, seller.getEmail()));

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

    dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    UpdateDayClassForm updateDayClassForm = UpdateDayClassForm.builder()
        .dayClassName("빵 클래스")
        .toChangeDayClassName("찐빵 클래스")
        .toChangeExplains("냄새도 좋은 빵")
        .toChangePrice(2000)
        .build();

    //when
    CustomException customException = assertThrows(CustomException.class,
        () -> dayClassService.updateDayClass(updateDayClassForm, seller.getEmail()));

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

    dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    //when
    List<DayClassDto> dayClassDtoList =
        dayClassService.getDayClassByEmail(seller.getEmail());

    //then
    assertEquals(dayClassDtoList.size(), 2);
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

    dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DeleteDayClassForm deleteDayClassForm = DeleteDayClassForm.builder()
        .dayClassName("마카롱 클래스").build();

    //when
    dayClassService.deleteDayClass(deleteDayClassForm, seller.getEmail());

    CustomException customException =
        assertThrows(CustomException.class,
            () -> dayClassRepository.findByStoreIdAndDayClassName(store.getId(), "마카롱 클래스")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS)));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_DAYCLASS);
  }

  @Test
  @DisplayName("데이클래스 전부 가져오기 성공")
  void success_getAllDayClass() {
    //given
    dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .build());

    dayClassRepository.save(DayClass.builder()
        .dayClassName("빵 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .build());

    //when
    List<DayClassDto> dayClassDtoList =
        dayClassService.getAllDayClass(Pageable.ofSize(2));

    //then
    assertEquals(dayClassDtoList.size(), 2);

  }

  @Test
  @DisplayName("데이클래스 이름으로 전부 가져오기 성공")
  void success_getAllDayClassByDayClassName() {
    //given
    dayClassRepository.save(DayClass.builder()
        .dayClassName("success_getAllDayClassByDayClassName1")
        .explains("success_getAllDayClassByDayClassName")
        .price(5000)
        .build());

    dayClassRepository.save(DayClass.builder()
        .dayClassName("success_getAllDayClassByDayClassName2")
        .explains("success_getAllDayClassByDayClassName")
        .price(5000)
        .build());

    //when
    List<DayClassDto> dayClassDtoList =
        dayClassService.getAllDayClassByDayClassName(
            "getAllDayClassByDayClassName", Pageable.ofSize(5));

    //then
    assertEquals(dayClassDtoList.size(), 2);
    assertEquals(dayClassDtoList.get(0).getExplains(), "success_getAllDayClassByDayClassName");
    assertEquals(dayClassDtoList.get(1).getExplains(), "success_getAllDayClassByDayClassName");

  }

  @Test
  @DisplayName("데이클래스 이름으로 전부 가져오기 성공")
  void success_getAllDayClassByStoreId() {
    //given

    Store store = storeRepository.save(Store.builder()
        .explains("")
        .category(Category.DESSERT)
        .build());

    dayClassRepository.save(DayClass.builder()
        .dayClassName("success_getAllDayClassByStoreId1")
        .explains("success_getAllDayClassByStoreId")
        .price(5000)
        .store(store)
        .build());

    dayClassRepository.save(DayClass.builder()
        .dayClassName("success_getAllDayClassByStoreId2")
        .explains("success_getAllDayClassByStoreId")
        .price(5000)
        .store(store)
        .build());

    //when
    List<DayClassDto> dayClassDtoList =
        dayClassService.getAllDayClassByStoreId(
            store.getId(), Pageable.ofSize(5));

    //then
    assertEquals(dayClassDtoList.size(), 2);
    assertEquals(dayClassDtoList.get(0).getExplains(), "success_getAllDayClassByStoreId");
    assertEquals(dayClassDtoList.get(1).getExplains(), "success_getAllDayClassByStoreId");

  }


}