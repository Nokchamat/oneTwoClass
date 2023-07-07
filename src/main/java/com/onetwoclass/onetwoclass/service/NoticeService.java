package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.NoticeDto;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Notice;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.notice.AddNoticeForm;
import com.onetwoclass.onetwoclass.domain.form.notice.DeleteNoticeForm;
import com.onetwoclass.onetwoclass.domain.form.notice.UpdateNoticeForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.NoticeRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final MemberRepository memberRepository;

  private final NoticeRepository noticeRepository;

  private final StoreRepository storeRepository;

  public void addNotice(AddNoticeForm addNoticeForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    noticeRepository.save(Notice.builder()
        .subject(addNoticeForm.getSubject())
        .text(addNoticeForm.getText())
        .store(store)
        .build());
  }


  public List<NoticeDto> getNoticeBySellerEmail(Pageable pageable, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    return noticeRepository.findAllByStoreId(store.getId(), pageable)
        .stream().map(Notice::toNoticeDto).collect(Collectors.toList());
  }

  public void deleteNotice(DeleteNoticeForm deleteNoticeForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    Notice notice = noticeRepository.findById(deleteNoticeForm.getNoticeId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTICE));

    if (notice.getStore().getId() != store.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_NOTICE);
    }

    noticeRepository.delete(notice);
  }

  @Transactional
  public void updateNotice(UpdateNoticeForm updateNoticeForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    Notice notice = noticeRepository.findById(updateNoticeForm.getNoticeId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTICE));

    if (notice.getStore().getId() != store.getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_NOTICE);
    }

    notice.updateNotice(updateNoticeForm);
  }

  public List<NoticeDto> getNoticeByStoreId(Pageable pageable, Long storeId) {

    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    return noticeRepository.findAllByStoreId(store.getId(), pageable)
        .stream().map(Notice::toNoticeDto).collect(Collectors.toList());
  }

}