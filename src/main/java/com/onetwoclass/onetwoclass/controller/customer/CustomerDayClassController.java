package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.service.DayClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/dayclass")
@RequiredArgsConstructor
public class CustomerDayClassController {

  private final DayClassService dayClassService;

  @GetMapping
  ResponseEntity<?> getAllDayClassByDayClass(Pageable pageable) {

    return ResponseEntity.ok(dayClassService.getAllDayClassFromElasticsearch(pageable));
  }

  @GetMapping("/store/{storeId}")
  ResponseEntity<?> getAllDayClassByStoreId(@PathVariable Long storeId, Pageable pageable) {

    return ResponseEntity.ok(dayClassService.getAllDayClassByStoreId(storeId, pageable));
  }

  @GetMapping("/name/{dayClassname}")
  ResponseEntity<?> getAllDayClassByDayClassName(@PathVariable String dayClassname,
      Pageable pageable) {

    return ResponseEntity.ok(
        dayClassService.getAllDayClassByDayClassNameFromElasticsearch(dayClassname, pageable));
  }

  @GetMapping("/id/{dayClassId}")
  ResponseEntity<?> getAllDayClassById(@PathVariable String dayClassId) {

    return ResponseEntity.ok(dayClassService.getDayClassDocumentFromElasticsearch(dayClassId));
  }


}
