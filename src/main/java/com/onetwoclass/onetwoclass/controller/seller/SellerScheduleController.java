package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.service.ScheduleService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping
  ResponseEntity<?> getScheduleBySchedulerIdAndDate(HttpServletRequest request,
      @RequestParam(value = "id") Long dayClassSchedulerId, Pageable pageable) {

    return ResponseEntity.ok(scheduleService.getAllScheduleBySellerEmailAndDayClassSchedulerId(
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)),
        dayClassSchedulerId, pageable));
  }

  @PostMapping
  ResponseEntity<?> acceptScheduleRequest(@RequestParam(value = "id") Long scheduleId,
      HttpServletRequest request) {

    scheduleService.acceptScheduleRequest(
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)), scheduleId);

    return ResponseEntity.ok("스케쥴 요청 처리가 완료되었습니다.");
  }


}
