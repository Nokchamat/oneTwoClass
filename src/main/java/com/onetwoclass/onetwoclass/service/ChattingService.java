package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.ChattingDto;
import com.onetwoclass.onetwoclass.domain.entity.Chatting;
import com.onetwoclass.onetwoclass.domain.entity.ChattingRoom;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.chatting.PostChattingForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.ChattingRepository;
import com.onetwoclass.onetwoclass.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {

  private final ChattingRoomRepository chattingRoomRepository;

  private final ChattingRepository chattingRepository;

  public void postChatting(Member member, PostChattingForm postChattingForm) {

    ChattingRoom chattingRoom =
        chattingRoomRepository.findById(postChattingForm.getChattingRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    if (chattingRoom.getExitCustomerYn() || chattingRoom.getExitSellerYn()) {
      throw new CustomException(ErrorCode.NOT_EXIST_OTHERS);
    }

    chattingRepository.save(Chatting.builder()
        .chattingRoom(chattingRoom)
        .postMember(member)
        .text(postChattingForm.getText())
        .build());

  }

  public Page<ChattingDto> getCustomerChattingByChattingRoomId(
      Long chattingRoomId, Member customer, Pageable pageable) {

    ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    if (chattingRoom.getCustomer().getId() != customer.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM);
    }

    return chattingRepository.findAllByChattingRoomId(chattingRoomId, pageable)
        .map(Chatting::toChattingDto);
  }

  public Page<ChattingDto> getSellerChattingByChattingRoomId(
      Long chattingRoomId, Member seller, Pageable pageable) {

    ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    if (chattingRoom.getSeller().getId() != seller.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM);
    }

    return chattingRepository.findAllByChattingRoomId(chattingRoomId, pageable)
        .map(Chatting::toChattingDto);
  }
}
