package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.notice.AddNoticeForm;
import com.onetwoclass.onetwoclass.domain.form.notice.DeleteNoticeForm;
import com.onetwoclass.onetwoclass.domain.form.notice.UpdateNoticeForm;
import com.onetwoclass.onetwoclass.service.NoticeService;
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
@RequestMapping("/api/v1/seller/notice")
@RequiredArgsConstructor
public class SellerNoticeController {

  private final NoticeService noticeService;

  @PostMapping
  ResponseEntity<?> addNotice(@RequestBody @Valid AddNoticeForm addNoticeForm,
      @AuthenticationPrincipal Member seller) {

    noticeService.addNotice(addNoticeForm, seller);

    return ResponseEntity.ok("공지사항 추가가 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getNotice(@AuthenticationPrincipal Member seller, Pageable pageable) {

    return ResponseEntity.ok(noticeService.getNoticeBySellerEmail(pageable, seller));
  }

  @DeleteMapping
  ResponseEntity<?> deleteNotice(@RequestBody @Valid DeleteNoticeForm deleteNoticeForm,
      @AuthenticationPrincipal Member seller) {

    noticeService.deleteNotice(deleteNoticeForm, seller);

    return ResponseEntity.ok("공지사항 삭제가 완료되었습니다.");
  }

  @PutMapping
  ResponseEntity<?> updateNotice(@RequestBody @Valid UpdateNoticeForm updateNoticeForm,
      @AuthenticationPrincipal Member seller) {

    noticeService.updateNotice(updateNoticeForm, seller);

    return ResponseEntity.ok("공지사항 업데이트가 완료되었습니다.");
  }


}
