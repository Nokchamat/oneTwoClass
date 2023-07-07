package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/notice")
@RequiredArgsConstructor
public class CustomerNoticeController {

  private final NoticeService noticeService;

  @GetMapping("/{storeId}")
  ResponseEntity<?> getNotice(@PathVariable Long storeId, Pageable pageable) {

    return ResponseEntity.ok(noticeService.getNoticeByStoreId(pageable, storeId));
  }

}
