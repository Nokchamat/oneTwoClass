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
  ResponseEntity<?> getAllDayClass(Pageable pageable) {

    return ResponseEntity.ok(dayClassService.getAllDayClass(pageable));
  }

  @GetMapping("/store/{storeId}")
  ResponseEntity<?> getAllDayClassByStoreId(@PathVariable Long storeId, Pageable pageable) {

    return ResponseEntity.ok(dayClassService.getAllDayClassByStoreId(storeId, pageable));
  }

  @GetMapping("/search/name/{dayClassname}")
  ResponseEntity<?> getAllDayClassByDayClassNameES(@PathVariable String dayClassname,
      Pageable pageable) {

    System.out.println("GetAllByNam : " + dayClassname);

    return ResponseEntity.ok(dayClassService.getAllDayClassByDayClassNameFromElasticsearch(dayClassname, pageable));
  }

  @GetMapping("/search/all")
  ResponseEntity<?> getAllDayClassByDayClassEsAll(Pageable pageable) {

    return ResponseEntity.ok(dayClassService.getAllDayClassEsAll(pageable));
  }

  @GetMapping("/search/detail/{dayClassId}")
  ResponseEntity<?> getAllDayClassByIdES(@PathVariable Long dayClassId) {

    return ResponseEntity.ok(dayClassService.getDayClassDocumentById(dayClassId));
  }


}
