package com.onetwoclass.onetwoclass.service;


import com.onetwoclass.onetwoclass.domain.dto.ReviewDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Review;
import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import com.onetwoclass.onetwoclass.domain.form.review.AddReviewForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.ReviewRepository;
import com.onetwoclass.onetwoclass.repository.ScheduleRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final MemberRepository memberRepository;

  private final DayClassRepository dayClassRepository;

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  private final ScheduleRepository scheduleRepository;

  public void addReview(AddReviewForm addReviewForm, String email) {

    Member customer = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Schedule schedule = scheduleRepository.findById(addReviewForm.getScheduleId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.findById(schedule.getDayClassScheduler().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_SCHEDULER));

    DayClass dayClass = dayClassRepository.findById(dayClassScheduler.getDayClass().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (!schedule.getAcceptYn()) {
      throw new CustomException(ErrorCode.NOT_VISITED_DAYCLASS);
    }

    if (customer.getId() != schedule.getCustomer().getId()) {
      throw new CustomException(ErrorCode.NOT_VISITED_DAYCLASS);
    }

    reviewRepository.findByScheduleId(schedule.getId())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
        });

    reviewRepository.save(Review.builder()
        .text(addReviewForm.getText())
        .star(addReviewForm.getStar())
        .dayClass(dayClass)
        .customer(customer)
        .schedule(schedule)
        .build());

  }

  public List<ReviewDto> getReviewByCustomerEmail(String email, Pageable pageable) {

    Member customer = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    return reviewRepository.findAllByCustomerId(customer.getId(), pageable)
        .stream().map(Review::toReviewDto).collect(Collectors.toList());
  }

  public  List<ReviewDto> getReviewByDayClassId(Long dayClassId, Pageable pageable) {

    return reviewRepository.findAllByDayClassId(dayClassId, pageable)
        .stream().map(Review::toReviewDto).collect(Collectors.toList());
  }

}
