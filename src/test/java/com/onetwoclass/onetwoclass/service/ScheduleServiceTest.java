package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.ScheduleDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.schedule.RequestScheduleForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.ScheduleRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class ScheduleServiceTest {

  @Autowired
  private DayClassSchedulerRepository dayClassSchedulerRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DayClassRepository dayClassRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ScheduleService scheduleService;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Test
  @DisplayName("스케쥴 요청 성공")
  void success_requestSchedule() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_requestSchedule@test.com")
        .name("강성혁")
        .phone("010-2123-2121")
        .password("12345678")
        .role(Role.CUSTOMER)
        .build());

    Member seller = memberRepository.save(Member.builder()
        .email("success_requestScheduleSeller@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClass(dayClass)
            .scheduledDate(LocalDateTime.now().plusDays(3))
            .build());

    RequestScheduleForm requestScheduleForm = RequestScheduleForm.builder()
        .dayClassSchedulerId(dayClassScheduler.getId())
        .build();

    //when
    scheduleService.requestSchedule(requestScheduleForm, customer);

    Schedule schedule =
        scheduleRepository.findByCustomerIdAndDayClassSchedulerId(
                customer.getId(), dayClassScheduler.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    //then
    assertEquals(schedule.getCustomer().getId(), customer.getId());
    assertEquals(schedule.getRequestedDateTime(), dayClassScheduler.getScheduledDate());

  }

  @Test
  @DisplayName("스케쥴 요청 실패 - 이미 요청된 스케쥴")
  void fail_requestSchedule_AlreadyRequestedSchedule() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("fail_requestSchedule_AlreadyRequestedSchedule@test.com")
        .name("강성혁")
        .phone("010-2123-2121")
        .password("12345678")
        .role(Role.CUSTOMER)
        .build());

    Member seller = memberRepository.save(Member.builder()
        .email("fail_requestSchedule_AlreadyRequestedSchedule2@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClass(dayClass)
            .scheduledDate(LocalDateTime.now().plusDays(3))
            .build());

    RequestScheduleForm requestScheduleForm = RequestScheduleForm.builder()
        .dayClassSchedulerId(dayClassScheduler.getId())
        .build();

    //when
    scheduleService.requestSchedule(requestScheduleForm, customer);

    CustomException customException = assertThrows(CustomException.class,
        () -> scheduleService.requestSchedule(requestScheduleForm, customer));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_REQUESTED_SCHEDULE);

  }

  @Test
  @DisplayName("커스터머 예약 내역 가져오기 성공")
  void success_getAllScheduleByCustomerEmail() {
    //given
    Member customer = memberRepository.save(Member.builder()
        .email("success_getAllScheduleByCustomerEmail@test.com")
        .name("강성혁")
        .phone("010-2123-2121")
        .password("12345678")
        .role(Role.CUSTOMER)
        .build());

    Member seller = memberRepository.save(Member.builder()
        .email("success_getAllScheduleByCustomerEmail2@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClassScheduler dayClassScheduler1 =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .scheduledDate(LocalDateTime.of(2023, 8, 20, 13, 0, 0))
            .dayClass(dayClass)
            .build());

    DayClassScheduler dayClassScheduler2 =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .scheduledDate(LocalDateTime.of(2023, 8, 21, 13, 0, 0))
            .dayClass(dayClass)
            .build());

    scheduleRepository.save(Schedule.builder()
        .customer(customer)
        .acceptYn(false)
        .requestedDateTime(dayClassScheduler1.getScheduledDate())
        .dayClassScheduler(dayClassScheduler1)
        .build());

    scheduleRepository.save(Schedule.builder()
        .customer(customer)
        .acceptYn(false)
        .requestedDateTime(dayClassScheduler2.getScheduledDate())
        .dayClassScheduler(dayClassScheduler2)
        .build());

    //when
    List<ScheduleDto> scheduleDtoList = scheduleService.getAllScheduleByCustomerEmail(
        customer, Pageable.unpaged());

    //then
    assertEquals(scheduleDtoList.size(), 2);

  }

  @Test
  @DisplayName("스케쥴러 예약 내역 가져오기 성공")
  void success_getAllScheduleBySellerEmailAndDayClassSchedulerId() {
    //given
    Member customer1 = memberRepository.save(Member.builder()
        .email("success_getAllScheduleBySellerEmailAndDayClassSchedulerId1@test.com")
        .name("강성혁")
        .phone("010-2123-2121")
        .password("12345678")
        .role(Role.CUSTOMER)
        .build());

    Member customer2 = memberRepository.save(Member.builder()
        .email("success_getAllScheduleBySellerEmailAndDayClassSchedulerId3@test.com")
        .name("강성혁")
        .phone("010-2123-2121")
        .password("12345678")
        .role(Role.CUSTOMER)
        .build());

    Member seller = memberRepository.save(Member.builder()
        .email("success_getAllScheduleBySellerEmailAndDayClassSchedulerId2@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .scheduledDate(LocalDateTime.of(2023, 8, 20, 13, 0, 0))
            .dayClass(dayClass)
            .build());

    scheduleRepository.save(Schedule.builder()
        .customer(customer1)
        .acceptYn(false)
        .requestedDateTime(dayClassScheduler.getScheduledDate())
        .dayClassScheduler(dayClassScheduler)
        .build());

    scheduleRepository.save(Schedule.builder()
        .customer(customer2)
        .acceptYn(false)
        .requestedDateTime(dayClassScheduler.getScheduledDate())
        .dayClassScheduler(dayClassScheduler)
        .build());

    //when
    List<ScheduleDto> scheduleDtoList = scheduleService.getAllScheduleBySellerEmailAndDayClassSchedulerId(
        seller, dayClassScheduler.getId(), Pageable.unpaged());

    //then
    assertEquals(scheduleDtoList.size(), 2);

  }

  @Test
  @DisplayName("요청된 예약 받기 성공")
  void success_acceptScheduleRequest() {
    //given
    Member customer1 = memberRepository.save(Member.builder()
        .email("success_acceptScheduleRequest1@test.com")
        .name("강성혁")
        .phone("010-2123-2121")
        .password("12345678")
        .role(Role.CUSTOMER)
        .build());

    Member seller = memberRepository.save(Member.builder()
        .email("success_acceptScheduleRequest2@test.com")
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

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName("마카롱 클래스")
        .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
        .price(5000)
        .store(store)
        .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .scheduledDate(LocalDateTime.of(2023, 8, 20, 13, 0, 0))
            .dayClass(dayClass)
            .build());

    Schedule schedule1 = scheduleRepository.save(Schedule.builder()
        .customer(customer1)
        .acceptYn(false)
        .requestedDateTime(dayClassScheduler.getScheduledDate())
        .dayClassScheduler(dayClassScheduler)
        .build());

    boolean yetYn = schedule1.getAcceptYn();

    //when
    scheduleService.acceptScheduleRequest(seller, schedule1.getId());

    Schedule schedule2 = scheduleRepository.findById(schedule1.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    //then
    assertFalse(yetYn);
    assertTrue(schedule2.getAcceptYn());

  }


}