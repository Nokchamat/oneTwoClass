package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.storebookmark.AddStoreBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.storebookmark.DeleteStoreBookmarkForm;
import com.onetwoclass.onetwoclass.service.StoreBookmarkService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  ResponseEntity<?> addStoreBookmark(@RequestBody AddStoreBookmarkForm addStoreBookmarkForm,
      HttpServletRequest request) {

    storeBookmarkService.addStoreBookmark(addStoreBookmarkForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("스토어 북마크 추가가 완료되었습니다.");
  }

  @DeleteMapping
  ResponseEntity<?> deleteStoreBookmark(
      @RequestBody DeleteStoreBookmarkForm deleteStoreBookmarkForm,
      HttpServletRequest request) {

    storeBookmarkService.deleteStoreBookmark(deleteStoreBookmarkForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("스토어 북마크 삭제가 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getStoreBookmark(HttpServletRequest request, Pageable pageable) {

    return ResponseEntity.ok(storeBookmarkService.getStoreBookmark(
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)), pageable));
  }

}
