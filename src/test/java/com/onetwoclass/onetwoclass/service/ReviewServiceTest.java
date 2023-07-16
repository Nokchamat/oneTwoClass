package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
import com.onetwoclass.onetwoclass.domain.dto.ReviewDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Review;
import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.review.AddReviewForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.ReviewRepository;
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
class ReviewServiceTest {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DayClassSearchRepository dayClassSearchRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private DayClassSchedulerRepository dayClassSchedulerRepository;

  @Test
  @DisplayName("리뷰 작성 성공")
  void success_addReview() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_addReviewSeller@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member customer = memberRepository.save(Member.builder()
        .email("success_addReviewcustomer@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassNameKeyword("빵 클래스")
            .dayClassNameText("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.now().minusDays(3))
            .build());

    Schedule schedule = scheduleRepository.save(Schedule.builder()
        .acceptYn(true)
        .dayClassScheduler(dayClassScheduler)
        .customer(customer)
        .build());

    AddReviewForm addReviewForm = AddReviewForm.builder()
        .scheduleId(schedule.getId())
        .star(5)
        .text("너무 맛있어요 !!")
        .build();

    //when
    reviewService.addReview(addReviewForm, customer);

    Review review = reviewRepository.findByScheduleId(schedule.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.ALREADY_EXIST_REVIEW));

    //then
    assertEquals(review.getStar(), addReviewForm.getStar());
    assertEquals(review.getText(), addReviewForm.getText());
    assertEquals(addReviewForm.getScheduleId(), schedule.getId());
  }

  @Test
  @DisplayName("리뷰 작성 실패 - 방문하지 않은 데이클래스")
  void fail_addReview_NotVisitedDayClass() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_addReview_NotVisitedDayClassseller@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member customer = memberRepository.save(Member.builder()
        .email("fail_addReview_NotVisitedDayClasscustomer@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassNameKeyword("빵 클래스")
            .dayClassNameText("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.now().plusDays(3))
            .build());

    Schedule schedule = scheduleRepository.save(Schedule.builder()
        .acceptYn(false)
        .dayClassScheduler(dayClassScheduler)
        .customer(customer)
        .build());

    AddReviewForm addReviewForm = AddReviewForm.builder()
        .scheduleId(schedule.getId())
        .star(5)
        .text("너무 맛있어요 !!")
        .build();

    //when
    CustomException customException = assertThrows(
        CustomException.class, () -> reviewService.addReview(addReviewForm, customer));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.NOT_VISITED_DAYCLASS);
  }

  @Test
  @DisplayName("리뷰 작성 실패 - 이미 작성한 리뷰")
  void fail_addReview_AlreadyExistReview() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("fail_addReview_AlreadyExistReviews@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member customer = memberRepository.save(Member.builder()
        .email("fail_addReview_AlreadyExistReviewc@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassNameKeyword("빵 클래스")
            .dayClassNameText("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.now().minusDays(3))
            .build());

    Schedule schedule = scheduleRepository.save(Schedule.builder()
        .acceptYn(true)
        .dayClassScheduler(dayClassScheduler)
        .customer(customer)
        .build());

    AddReviewForm addReviewForm = AddReviewForm.builder()
        .scheduleId(schedule.getId())
        .star(5)
        .text("너무 맛있어요 !!")
        .build();

    //when
    reviewService.addReview(addReviewForm, customer);

    CustomException customException = assertThrows(
        CustomException.class, () -> reviewService.addReview(addReviewForm, customer));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_EXIST_REVIEW);
  }

  @Test
  @DisplayName("커스터머 아이디로 리뷰 가져오기")
  void success_getReviewByCustomerEmail() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_getReviewByCustomerEmails@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member customer = memberRepository.save(Member.builder()
        .email("success_getReviewByCustomerEmailc@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassNameKeyword("빵 클래스")
            .dayClassNameText("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.now().plusDays(3))
            .build());

    Schedule schedule = scheduleRepository.save(Schedule.builder()
        .acceptYn(true)
        .dayClassScheduler(dayClassScheduler)
        .customer(customer)
        .build());

    //when
    Review review = reviewRepository.save(Review.builder()
        .schedule(schedule)
        .customer(customer)
        .dayClassId(dayClassDocument.getId())
        .text("너무 맛있어요!")
        .star(5)
        .build());

    List<ReviewDto> reviewDtoList =
        reviewService.getReviewByCustomer(customer, Pageable.unpaged()).getContent();

    //then
    assertEquals(reviewDtoList.get(0).getDayClassId(), review.getDayClassId());
    assertEquals(reviewDtoList.get(0).getScheduleId(), review.getSchedule().getId());
    assertEquals(reviewDtoList.get(0).getText(), review.getText());
    assertEquals(reviewDtoList.get(0).getStar(), review.getStar());
  }

  @Test
  @DisplayName("커스터머 아이디로 리뷰 가져오기")
  void success_getReviewByDayClassId() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_getReviewByDayClassIds@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member customer = memberRepository.save(Member.builder()
        .email("success_getReviewByDayClassIdc@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    DayClassDocument dayClassDocument = dayClassSearchRepository.save(
        DayClassDocument.builder()
            .dayClassNameKeyword("빵 클래스")
            .dayClassNameText("빵 클래스")
            .explains("여러 마카롱을 맛볼 수 있고 만들어요 !")
            .price(5000)
            .storeId(store.getId())
            .build());

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.save(DayClassScheduler.builder()
            .dayClassId(dayClassDocument.getId())
            .scheduledDate(LocalDateTime.now().plusDays(3))
            .build());

    Schedule schedule = scheduleRepository.save(Schedule.builder()
        .acceptYn(true)
        .dayClassScheduler(dayClassScheduler)
        .customer(customer)
        .build());

    //when
    Review review = reviewRepository.save(Review.builder()
        .schedule(schedule)
        .customer(customer)
        .dayClassId(dayClassDocument.getId())
        .text("너무 맛있어요!")
        .star(5)
        .build());

    List<ReviewDto> reviewDtoList =
        reviewService.getReviewByDayClassId(dayClassDocument.getId(), Pageable.unpaged())
            .getContent();

    //then
    assertEquals(reviewDtoList.get(0).getDayClassId(), review.getDayClassId());
    assertEquals(reviewDtoList.get(0).getScheduleId(), review.getSchedule().getId());
    assertEquals(reviewDtoList.get(0).getText(), review.getText());
    assertEquals(reviewDtoList.get(0).getStar(), review.getStar());
  }


}