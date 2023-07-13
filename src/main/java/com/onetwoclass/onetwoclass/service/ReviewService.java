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
import com.onetwoclass.onetwoclass.repository.ReviewRepository;
import com.onetwoclass.onetwoclass.repository.ScheduleRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  private final DayClassRepository dayClassRepository;

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  private final ScheduleRepository scheduleRepository;

  public void addReview(AddReviewForm addReviewForm, Member customer) {

    Schedule schedule = scheduleRepository.findById(addReviewForm.getScheduleId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.findById(schedule.getDayClassScheduler().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_SCHEDULER));

    DayClass dayClass = dayClassRepository.findById(dayClassScheduler.getDayClass().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (!schedule.getAcceptYn() || !dayClassScheduler.getScheduledDate().isBefore(LocalDateTime.now())) {
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

  public Page<ReviewDto> getReviewByCustomer(Member customer, Pageable pageable) {

    return reviewRepository.findAllByCustomerId(customer.getId(), pageable)
        .map(Review::toReviewDto);
  }

  public  Page<ReviewDto> getReviewByDayClassId(Long dayClassId, Pageable pageable) {

    return reviewRepository.findAllByDayClassId(dayClassId, pageable)
        .map(Review::toReviewDto);
  }

  @Cacheable(key = "#dayClassId", value = "getDayClassStarScore")
  public Double getDayClassStarScore(Long dayClassId) {

    List<Review> reviewList = reviewRepository.findAllByDayClassId(dayClassId);

    if (reviewList.isEmpty()){
      return 0.0;
    }

    return reviewList.stream().mapToLong(Review::getStar).average().getAsDouble();
  }

}
