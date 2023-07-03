package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.StoreDto;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.store.AddStoreForm;
import com.onetwoclass.onetwoclass.domain.form.sign.SignUpForm;
import com.onetwoclass.onetwoclass.domain.form.store.UpdateStoreForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class StoreServiceTest {

  @Autowired
  private StoreService storeService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("상점 추가 성공")
  void Success_AddStore() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Success_AddStore@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build();

    AddStoreForm addStoreForm = AddStoreForm.builder()
        .storename("강식당")
        .explains("재밌게 만들고 맛있는 음식 !")
        .category(Category.DESSERT)
        .build();

    memberService.signUp(signUpForm);

    Member seller = memberRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    //when
    storeService.addStore(addStoreForm, seller.getEmail());

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    //then
    assertEquals(addStoreForm.getStorename(), store.getStoreName());
    assertEquals(addStoreForm.getExplains(), store.getExplains());
    assertEquals(addStoreForm.getCategory(), store.getCategory());

  }

  @Test
  @DisplayName("상점 추가 실패 - 이미 등록된 상점 존재")
  void Fail_AddStore_AlreadyExistStore() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Fail_AddStore_AlreadyExistStore@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build();

    AddStoreForm addStoreForm = AddStoreForm.builder()
        .storename("강식당")
        .explains("재밌게 만들고 맛있는 음식 !")
        .category(Category.DESSERT)
        .build();

    AddStoreForm afterAddStoreForm = AddStoreForm.builder()
        .storename("정말 다른 가게")
        .explains("이번엔 더 재밌네요 !")
        .category(Category.DESSERT)
        .build();

    memberService.signUp(signUpForm);

    Member seller = memberRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    //when
    storeService.addStore(addStoreForm, seller.getEmail());

    StoreDto storeDto = storeService.getStoreByEmail(seller.getEmail());
    System.out.println(storeDto.getStoreName());
    System.out.println(storeDto.getExplains());

    CustomException customException =
        assertThrows(CustomException.class,
            () -> storeService.addStore(afterAddStoreForm, seller.getEmail()));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_STORE);

  }

  @Test
  @DisplayName("가게 정보 수정 성공")
  void Success_UpdateStore() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Success_UpdateStore@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build();

    AddStoreForm addStoreForm = AddStoreForm.builder()
        .storename("강식당")
        .explains("재밌게 만들고 맛있는 음식 !")
        .category(Category.DESSERT)
        .build();

    UpdateStoreForm updateStoreForm =
        UpdateStoreForm.builder()
            .storename("정식당")
            .explains("너무 맛있게 바뀐 디저트 !")
            .category(Category.HANDICRAFT)
            .build();

    memberService.signUp(signUpForm);
    storeService.addStore(addStoreForm, signUpForm.getEmail());

    Member seller = memberRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    //when
    storeService.updateStore(updateStoreForm, signUpForm.getEmail());

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    //then
    assertEquals(updateStoreForm.getStorename(), store.getStoreName());
    assertEquals(updateStoreForm.getExplains(), store.getExplains());
    assertEquals(updateStoreForm.getCategory(), store.getCategory());

  }

  @Test
  @DisplayName("가게 정보 가져오기 성공")
  void Success_GetStore() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Success_GetStore@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build();

    AddStoreForm addStoreForm = AddStoreForm.builder()
        .storename("강식당")
        .explains("재밌게 만들고 맛있는 음식 !")
        .category(Category.DESSERT)
        .build();

    memberService.signUp(signUpForm);
    storeService.addStore(addStoreForm, signUpForm.getEmail());

    //when
    StoreDto storeDto = storeService.getStoreByEmail(signUpForm.getEmail());

    //then
    assertEquals(addStoreForm.getStorename(), storeDto.getStoreName());
    assertEquals(addStoreForm.getExplains(), storeDto.getExplains());
    assertEquals(addStoreForm.getCategory(), storeDto.getCategory());

  }

  @Test
  @DisplayName("가게 삭제하기 성공")
  void Success_DeleteStore() {
    //given
    SignUpForm signUpForm = SignUpForm.builder()
        .email("Success_DeleteStore@test.com")
        .name("홍길동")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build();

    AddStoreForm addStoreForm = AddStoreForm.builder()
        .storename("강식당")
        .explains("재밌게 만들고 맛있는 음식 !")
        .category(Category.DESSERT)
        .build();

    memberService.signUp(signUpForm);
    storeService.addStore(addStoreForm, signUpForm.getEmail());

    Member seller = memberRepository.findByEmail(signUpForm.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    //when
    storeService.deleteStore(signUpForm.getEmail());

    CustomException customException =
        assertThrows(CustomException.class,
            () -> storeRepository.findBySellerId(seller.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE)));

    //then
    assertNotNull(store);
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_STORE);

  }

  @Test
  @DisplayName("가게 정보 전부 가져오기 성공")
  void success_getAllStore() {
    //given
    storeRepository.save(Store.builder()
        .storeName("상점1")
        .category(Category.DESSERT)
        .explains("너무 맛있어요")
        .build());

    storeRepository.save(Store.builder()
        .storeName("상점2")
        .category(Category.DESSERT)
        .explains("너무 맛있어요")
        .build());

    //when
    List<StoreDto> storeDtoList = storeService.getAllStore(Pageable.ofSize(2));

    //then
    assertEquals(storeDtoList.size(), 2);

  }

  @Test
  @DisplayName("가게 정보 전부 이름으로 가져오기 성공")
  void success_getAllStoreByName() {
    //given
    storeRepository.save(Store.builder()
        .storeName("상점1")
        .category(Category.DESSERT)
        .explains("너무 맛있어요")
        .build());

    storeRepository.save(Store.builder()
        .storeName("상점2")
        .category(Category.DESSERT)
        .explains("너무 맛있어요")
        .build());

    //when
    List<StoreDto> storeDtoList = storeService.getAllStoreByName(Pageable.unpaged(), "2");

    //then
    assertEquals(storeDtoList.size(), 1);

  }



}