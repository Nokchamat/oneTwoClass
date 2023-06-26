package com.onetwoclass.onetwoclass.controller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.AddStoreForm;
import com.onetwoclass.onetwoclass.domain.form.UpdateStoreForm;
import com.onetwoclass.onetwoclass.service.StoreService;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  ResponseEntity<?> addStore(@RequestBody AddStoreForm addStoreForm,
      HttpServletRequest request) {

    storeService.addStore(addStoreForm,
        jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("상점 등록이 완료되었습니다.");
  }

  @PutMapping
  ResponseEntity<?> updateStore(@RequestBody UpdateStoreForm updateStoreForm,
      HttpServletRequest request) {

    storeService.updateStore(updateStoreForm,
        jwtTokenProvider.getMemberEmail(
            jwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("상점 정보 수정이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getStore(HttpServletRequest request) {

    return ResponseEntity.ok(storeService
        .getStore(jwtTokenProvider.getMemberEmail(
            jwtTokenProvider.resolveToken(request))));
  }

  @DeleteMapping
  ResponseEntity<?> deleteStore(HttpServletRequest request) {

    storeService.deleteStore(
        jwtTokenProvider.getMemberEmail(
            jwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("상점 삭제가 완료되었습니다.");
  }

}
