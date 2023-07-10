package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.chattingroom.CreateChattingRoomForm;
import com.onetwoclass.onetwoclass.domain.form.chattingroom.ExitChattingRoomForm;
import com.onetwoclass.onetwoclass.service.ChattingRoomService;
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
@RequestMapping("/api/v1/customer/chattingroom")
@RequiredArgsConstructor
public class CustomerChattingRoomController {

  private final ChattingRoomService chattingRoomService;

  @PostMapping
  ResponseEntity<?> createChattingRoom(@RequestBody CreateChattingRoomForm createChattingRoomForm,
      @AuthenticationPrincipal Member customer) {

    chattingRoomService.createChattingRoom(createChattingRoomForm, customer);

    return ResponseEntity.ok("채팅방 생성이 완료되었습니다.");
  }

  @DeleteMapping
  ResponseEntity<?> exitChattingRoom(@RequestBody ExitChattingRoomForm exitChattingRoomForm,
      @AuthenticationPrincipal Member customer) {

    chattingRoomService.exitChattingRoomByCustomer(exitChattingRoomForm, customer);

    return ResponseEntity.ok("채팅방을 나갔습니다.");
  }

  @GetMapping
  ResponseEntity<?> getChattingRoom(@AuthenticationPrincipal Member customer, Pageable pageable) {

    return ResponseEntity.ok(chattingRoomService.getChattingRoomByCustomer(customer, pageable));
  }

}
