package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.schedule.RequestScheduleForm;
import com.onetwoclass.onetwoclass.service.ScheduleService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/schedule")
@RequiredArgsConstructor
public class CustomerScheduleController {

  private final ScheduleService scheduleService;

  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  ResponseEntity<?> requestSchedule(@RequestBody RequestScheduleForm requestScheduleForm,
      HttpServletRequest request) {

    scheduleService.requestSchedule(requestScheduleForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("예약 요청이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getAllSchedule(HttpServletRequest request, Pageable pageable) {

    return ResponseEntity.ok(scheduleService.getAllScheduleByCustomerEmail(
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)), pageable));
  }


}
