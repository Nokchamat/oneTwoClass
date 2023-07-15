package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.review.AddReviewForm;
import com.onetwoclass.onetwoclass.service.ReviewService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/review")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping
  ResponseEntity<?> addReview(@RequestBody @Valid AddReviewForm addReviewForm,
      @AuthenticationPrincipal Member customer) {

    reviewService.addReview(addReviewForm, customer);

    return ResponseEntity.ok("리뷰 작성이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getReviewByCustomerEmail(@AuthenticationPrincipal Member customer,
      Pageable pageable) {

    return ResponseEntity.ok(reviewService.getReviewByCustomer(customer, pageable));
  }

  @GetMapping("/{dayClassId}")
  ResponseEntity<?> getReviewByDayClassId(@PathVariable String dayClassId, Pageable pageable) {

    return ResponseEntity.ok(reviewService.getReviewByDayClassId(dayClassId, pageable));
  }

}
