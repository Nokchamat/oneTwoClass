package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.store.AddStoreForm;
import com.onetwoclass.onetwoclass.domain.form.store.UpdateStoreForm;
import com.onetwoclass.onetwoclass.service.StoreService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/seller/store")
@RequiredArgsConstructor
public class SellerStoreController {

  private final StoreService storeService;

  @PostMapping
  ResponseEntity<?> addStore(@RequestBody @Valid AddStoreForm addStoreForm,
      @AuthenticationPrincipal Member seller) {

    storeService.addStore(addStoreForm, seller);

    return ResponseEntity.ok("상점 등록이 완료되었습니다.");
  }

  @PutMapping
  ResponseEntity<?> updateStore(@RequestBody @Valid UpdateStoreForm updateStoreForm,
      @AuthenticationPrincipal Member seller) {

    storeService.updateStore(updateStoreForm, seller);

    return ResponseEntity.ok("상점 정보 수정이 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getStore(@AuthenticationPrincipal Member seller) {

    return ResponseEntity.ok(storeService.getStoreBySeller(seller));
  }

  @DeleteMapping
  ResponseEntity<?> deleteStore(@AuthenticationPrincipal Member seller) {

    storeService.deleteStore(seller);

    return ResponseEntity.ok("상점 삭제가 완료되었습니다.");
  }

}
