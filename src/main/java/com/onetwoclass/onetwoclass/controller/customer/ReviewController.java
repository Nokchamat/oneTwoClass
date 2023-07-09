package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.review.AddReviewForm;
import com.onetwoclass.onetwoclass.service.ReviewService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  ResponseEntity<?> addReview(@RequestBody @Valid AddReviewForm addReviewForm,
      HttpServletRequest request) {

    reviewService.addReview(addReviewForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("리뷰 작성이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getReviewByCustomerEmail(HttpServletRequest request, Pageable pageable) {

    return ResponseEntity.ok(reviewService.getReviewByCustomerEmail(
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)), pageable));
  }

  @GetMapping("/{dayClassId}")
  ResponseEntity<?> getReviewByDayClassId(@PathVariable Long dayClassId, Pageable pageable) {

    return ResponseEntity.ok(reviewService.getReviewByDayClassId(dayClassId, pageable));
  }

}
