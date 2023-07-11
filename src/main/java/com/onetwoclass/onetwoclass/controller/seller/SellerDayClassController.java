package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.dayclass.AddDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.DeleteDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import com.onetwoclass.onetwoclass.service.DayClassService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seller/dayclass")
@RequiredArgsConstructor
public class SellerDayClassController {

  private final DayClassService dayClassService;

  @PostMapping
  ResponseEntity<?> addDayClass(@RequestBody @Valid AddDayClassForm addDayClassForm,
      @AuthenticationPrincipal Member seller) {

    dayClassService.addDayClass(addDayClassForm, seller);

    return ResponseEntity.ok("데이클래스 등록이 완료되었습니다.");
  }

  @PutMapping
  ResponseEntity<?> updateDayClass(@RequestBody @Valid UpdateDayClassForm updateDayClassForm,
      @AuthenticationPrincipal Member seller) {

    dayClassService.updateDayClass(updateDayClassForm, seller);

    return ResponseEntity.ok("데이클래스 정보 수정이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getDayClass(@AuthenticationPrincipal Member seller, Pageable pageable) {

    return ResponseEntity.ok(
        dayClassService.getDayClassBySeller(seller, pageable));
  }

  @DeleteMapping
  ResponseEntity<?> deleteDayClass(@AuthenticationPrincipal Member seller,
      @RequestBody DeleteDayClassForm deleteDayClassForm) {

    dayClassService.deleteDayClass(deleteDayClassForm, seller);

    return ResponseEntity.ok("데이클래스 삭제가 완료되었습니다.");
  }


}
