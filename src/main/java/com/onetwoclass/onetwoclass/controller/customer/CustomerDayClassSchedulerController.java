package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.service.DayClassSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/dayclassscheduler")
@RequiredArgsConstructor
public class CustomerDayClassSchedulerController {

  private final DayClassSchedulerService dayClassSchedulerService;

  @GetMapping("/{dayClassId}")
  ResponseEntity<?> getDayClassSchedulerByDayClassId(
      @PathVariable String dayClassId, Pageable pageable) {
    return ResponseEntity.ok(
        dayClassSchedulerService.getDayClassSchedulerByDayClassId(dayClassId, pageable));
  }

}
