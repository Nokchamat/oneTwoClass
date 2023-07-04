package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.dayclassscheduler.AddDayClassSchedulerForm;
import com.onetwoclass.onetwoclass.service.DayClassSchedulerService;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/seller/dayclassscheduler")
@RequiredArgsConstructor
public class SellerDayClassSchedulerController {

  private final DayClassSchedulerService dayClassSchedulerService;

  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  ResponseEntity<?> addDayClassScheduler(
      @RequestBody AddDayClassSchedulerForm addDayClassSchedulerForm,
      HttpServletRequest request) {

    dayClassSchedulerService.addDayClassScheduler(addDayClassSchedulerForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("스케쥴 추가가 완료되었습니다.");
  }

  @GetMapping("/{dayClassId}")
  ResponseEntity<?> getDayClassScheduler(@PathVariable Long dayClassId,
      HttpServletRequest request, Pageable pageable) {

    return ResponseEntity.ok(dayClassSchedulerService.getDayClassSchedulerByDayClassIdAndEmail(
        dayClassId, jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)),
        pageable));
  }

}
