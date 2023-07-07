package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.config.JwtTokenProvider;
import com.onetwoclass.onetwoclass.domain.form.notice.AddNoticeForm;
import com.onetwoclass.onetwoclass.domain.form.notice.DeleteNoticeForm;
import com.onetwoclass.onetwoclass.domain.form.notice.UpdateNoticeForm;
import com.onetwoclass.onetwoclass.service.NoticeService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  ResponseEntity<?> addNotice(@RequestBody AddNoticeForm addNoticeForm,
      HttpServletRequest request) {

    noticeService.addNotice(addNoticeForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("공지사항 추가가 완료되었습니다.");
  }

  @GetMapping
  ResponseEntity<?> getNotice(HttpServletRequest request, Pageable pageable) {

    return ResponseEntity.ok(noticeService.getNoticeBySellerEmail(pageable,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request))));
  }

  @DeleteMapping
  ResponseEntity<?> deleteNotice(@RequestBody DeleteNoticeForm deleteNoticeForm,
      HttpServletRequest request) {

    noticeService.deleteNotice(deleteNoticeForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("공지사항 삭제가 완료되었습니다.");
  }

  @PutMapping
  ResponseEntity<?> updateNotice(@RequestBody UpdateNoticeForm updateNoticeForm, HttpServletRequest request) {

    noticeService.updateNotice(updateNoticeForm,
        jwtTokenProvider.getMemberEmail(JwtTokenProvider.resolveToken(request)));

    return ResponseEntity.ok("공지사항 업데이트가 완료되었습니다.");
  }


}
