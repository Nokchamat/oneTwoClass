package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.AddDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.DeleteDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.service.DayClassBookmarkService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/dayclassbookmark")
@RequiredArgsConstructor
public class DayClassBookmarkController {

  private final DayClassBookmarkService dayclassBookmarkService;

  @PostMapping
  ResponseEntity<?> addDayClassBookmark(
      @RequestBody @Valid AddDayClassBookmarkForm addDayClassBookmarkForm,
      @AuthenticationPrincipal Member customer) {

    dayclassBookmarkService.addDayClassBookmark(addDayClassBookmarkForm, customer);

    return ResponseEntity.ok("데이클래스 북마크 추가가 완료되었습니다.");
  }

  @DeleteMapping
  ResponseEntity<?> deleteDayClassBookmark(
      @RequestBody @Valid DeleteDayClassBookmarkForm deleteDayClassBookmarkForm,
      @AuthenticationPrincipal Member customer) {

    dayclassBookmarkService.deleteDayClassBookmark(deleteDayClassBookmarkForm, customer);

    return ResponseEntity.ok("데이클래스 북마크 삭제가 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getDayClassBookmark(@AuthenticationPrincipal Member customer,
      Pageable pageable) {

    return ResponseEntity.ok(dayclassBookmarkService.getDayClassBookmark(customer, pageable));
  }

}
