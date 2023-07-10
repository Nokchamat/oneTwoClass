package com.onetwoclass.onetwoclass.controller.seller;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.chatting.PostChattingForm;
import com.onetwoclass.onetwoclass.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seller/chatting")
@RequiredArgsConstructor
public class SellerChattingController {

  private final ChattingService chattingService;

  @PostMapping
  ResponseEntity<?> postChatting(@RequestBody PostChattingForm postChattingForm,
      @AuthenticationPrincipal Member member) {

    chattingService.postChatting(member, postChattingForm);

    return ResponseEntity.ok("메시지를 보냈습니다.");
  }

  @GetMapping("/{chattingRoomId}")
  ResponseEntity<?> getChattingByChattingRoomId(@PathVariable Long chattingRoomId,
      @AuthenticationPrincipal Member seller, Pageable pageable) {

    return ResponseEntity.ok(
        chattingService.getSellerChattingByChattingRoomId(chattingRoomId, seller, pageable));
  }

}
