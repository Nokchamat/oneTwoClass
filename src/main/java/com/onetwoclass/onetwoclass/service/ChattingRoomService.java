package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.ChattingRoomDto;
import com.onetwoclass.onetwoclass.domain.entity.ChattingRoom;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.chattingroom.CreateChattingRoomForm;
import com.onetwoclass.onetwoclass.domain.form.chattingroom.ExitChattingRoomForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.ChattingRepository;
import com.onetwoclass.onetwoclass.repository.ChattingRoomRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomRepository chattingRoomRepository;

  private final ChattingRepository chattingRepository;

  private final MemberRepository memberRepository;

  private final StoreRepository storeRepository;

  public void createChattingRoom(CreateChattingRoomForm createChattingRoomForm, Member customer) {

    Store store = storeRepository.findById(createChattingRoomForm.getStoreId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    Member seller = memberRepository.findById(store.getSeller().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    chattingRoomRepository.findByCustomerIdAndSellerIdAndExitCustomerYnIsFalse(
        customer.getId(), seller.getId()).ifPresent(a -> {
      throw new CustomException(ErrorCode.ALREADY_EXIST_CHATTINGROOM);
    });

    chattingRoomRepository.save(ChattingRoom.builder()
        .customer(customer)
        .seller(seller)
        .storeName(store.getStoreName())
        .exitCustomerYn(false)
        .exitSellerYn(false)
        .build());
  }

  public void exitChattingRoomByCustomer(ExitChattingRoomForm chattingRoomForm, Member customer) {

    ChattingRoom chattingRoom =
        chattingRoomRepository.findById(chattingRoomForm.getChattingRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    if (chattingRoom.getExitCustomerYn()) {
      throw new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM);
    }

    if (chattingRoom.getCustomer().getId() != customer.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM);
    }

    chattingRoom.setExitCustomerYn();

    chattingRoomRepository.save(chattingRoom);

    if (chattingRoom.isEmptyRoom()) {
      chattingRepository.deleteAllByChattingRoomId(chattingRoom.getId());
      chattingRoomRepository.delete(chattingRoom);
    }

  }

  public void exitChattingRoomBySeller(ExitChattingRoomForm chattingRoomForm, Member seller) {

    ChattingRoom chattingRoom =
        chattingRoomRepository.findById(chattingRoomForm.getChattingRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM));

    if (chattingRoom.getExitSellerYn()) {
      throw new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM);
    }

    if (chattingRoom.getSeller().getId() != seller.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_CHATTINGROOM);
    }

    chattingRoom.setExitSellerYn();

    chattingRoomRepository.save(chattingRoom);

    if (chattingRoom.isEmptyRoom()) {
      chattingRepository.deleteAllByChattingRoomId(chattingRoom.getId());
      chattingRoomRepository.delete(chattingRoom);
    }

  }

  public Page<ChattingRoomDto> getChattingRoomByCustomer(Member customer, Pageable pageable) {

    return chattingRoomRepository.findAllByCustomerIdAndExitCustomerYnIsFalse(customer.getId(),
            pageable).map(ChattingRoom::toChattingRoomDto);
  }

  public Page<ChattingRoomDto> getChattingRoomBySeller(Member seller, Pageable pageable) {

    return chattingRoomRepository.findAllBySellerIdAndExitSellerYnIsFalse(seller.getId(),
            pageable).map(ChattingRoom::toChattingRoomDto);
  }

}
