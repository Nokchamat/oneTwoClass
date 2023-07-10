package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.storebookmark.AddStoreBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.storebookmark.DeleteStoreBookmarkForm;
import com.onetwoclass.onetwoclass.service.StoreBookmarkService;
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
@RequestMapping("/api/v1/customer/storebookmark")
@RequiredArgsConstructor
public class StoreBookmarkController {

  private final StoreBookmarkService storeBookmarkService;

  @PostMapping
  ResponseEntity<?> addStoreBookmark(@RequestBody @Valid AddStoreBookmarkForm addStoreBookmarkForm,
      @AuthenticationPrincipal Member customer) {

    storeBookmarkService.addStoreBookmark(addStoreBookmarkForm, customer);

    return ResponseEntity.ok("스토어 북마크 추가가 완료되었습니다.");
  }

  @DeleteMapping
  ResponseEntity<?> deleteStoreBookmark(
      @RequestBody @Valid DeleteStoreBookmarkForm deleteStoreBookmarkForm,
      @AuthenticationPrincipal Member customer) {

    storeBookmarkService.deleteStoreBookmark(deleteStoreBookmarkForm, customer);

    return ResponseEntity.ok("스토어 북마크 삭제가 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getStoreBookmark(@AuthenticationPrincipal Member customer, Pageable pageable) {

    return ResponseEntity.ok(storeBookmarkService.getStoreBookmark(customer, pageable));
  }

}
