package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seller/schedule")
@RequiredArgsConstructor
public class SellerScheduleController {

  private final ScheduleService scheduleService;

  @GetMapping
  ResponseEntity<?> getScheduleBySchedulerIdAndDate(@AuthenticationPrincipal Member seller,
      @RequestParam(value = "id") Long dayClassSchedulerId, Pageable pageable) {

    return ResponseEntity.ok(scheduleService.getAllScheduleBySellerEmailAndDayClassSchedulerId(
        seller, dayClassSchedulerId, pageable));
  }

  @PostMapping
  ResponseEntity<?> acceptScheduleRequest(@RequestParam(value = "id") Long scheduleId,
      @AuthenticationPrincipal Member seller) {

    scheduleService.acceptScheduleRequest(seller, scheduleId);

    return ResponseEntity.ok("스케쥴 요청 처리가 완료되었습니다.");
  }


}
