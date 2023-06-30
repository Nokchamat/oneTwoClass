package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.dayclass.AddDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.DeleteDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import com.onetwoclass.onetwoclass.service.DayClassService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  private final JwtTokenProvider jwtTokenProvider;

  private final DayClassService dayClassService;

  @PostMapping
  ResponseEntity<?> addDayClass(@RequestBody @Valid AddDayClassForm addDayClassForm,
      HttpServletRequest request) {

    dayClassService.addDayClass(addDayClassForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("데이클래스 등록이 완료되었습니다.");
  }

  @PutMapping
  ResponseEntity<?> updateDayClass(@RequestBody UpdateDayClassForm updateDayClassForm,
      HttpServletRequest request) {

    dayClassService.updateDayClass(updateDayClassForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("데이클래스 정보 수정이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getDayClass(HttpServletRequest request) {

    return ResponseEntity.ok(
        dayClassService.getDayClassByEmail(
            jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request))));
  }

  @DeleteMapping
  ResponseEntity<?> deleteDayClass(HttpServletRequest request,
      @RequestBody DeleteDayClassForm deleteDayClassForm) {

    dayClassService.deleteDayClass(deleteDayClassForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("데이클래스 삭제가 완료되었습니다.");
  }


}
