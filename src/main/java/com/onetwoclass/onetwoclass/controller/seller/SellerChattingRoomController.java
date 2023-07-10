package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.chattingroom.ExitChattingRoomForm;
import com.onetwoclass.onetwoclass.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seller/chattingroom")
@RequiredArgsConstructor
public class SellerChattingRoomController {

  private final ChattingRoomService chattingRoomService;

  @DeleteMapping
  ResponseEntity<?> exitChattingRoom(@RequestBody ExitChattingRoomForm exitChattingRoomForm,
      @AuthenticationPrincipal Member seller) {

    chattingRoomService.exitChattingRoomBySeller(exitChattingRoomForm, seller);

    return ResponseEntity.ok("채팅방을 나갔습니다.");
  }

  @GetMapping
  ResponseEntity<?> getChattingRoom(@AuthenticationPrincipal Member seller, Pageable pageable) {

    return ResponseEntity.ok(chattingRoomService.getChattingRoomBySeller(seller, pageable));
  }

}
