package com.onetwoclass.onetwoclass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import com.onetwoclass.onetwoclass.domain.constants.Role;
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
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class NoticeServiceTest {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private NoticeService noticeService;

  @Autowired
  private NoticeRepository noticeRepository;

  @Test
  @DisplayName("공지사항 추가 성공")
  void success_addNotice() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_addNotice@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    AddNoticeForm addNoticeForm = AddNoticeForm.builder()
        .subject("공지사항 !")
        .text("텍스트 테스트 @")
        .build();

    //when
    noticeService.addNotice(addNoticeForm, seller);

    Page<Notice> noticeList =
        noticeRepository.findAllByStoreId(store.getId(), Pageable.unpaged());

    //then
    assertEquals(noticeList.getContent().get(0).getSubject(), addNoticeForm.getSubject());
    assertEquals(noticeList.getContent().get(0).getText(), addNoticeForm.getText());
  }

  @Test
  @DisplayName("판매자 이메일로 공지사항 가져오기 성공")
  void success_getNoticeBySellerEmail() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_getNotice@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목1")
        .text("텍스트1")
        .store(store)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목2")
        .text("텍스트2")
        .store(store)
        .build());

    //when
    Page<NoticeDto> noticeDtoList =
        noticeService.getNoticeBySellerEmail(Pageable.unpaged(), seller);

    //then
    assertEquals(noticeDtoList.getTotalElements(), 2);
  }

  @Test
  @DisplayName("공지사항 삭제 성공")
  void success_deleteNotice() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_deleteNotice@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    Notice notice = noticeRepository.save(Notice.builder()
        .subject("제목1")
        .text("텍스트1")
        .store(store)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목2")
        .text("텍스트2")
        .store(store)
        .build());

    DeleteNoticeForm deleteNoticeForm = DeleteNoticeForm.builder()
        .noticeId(notice.getId())
        .build();

    //when
    Page<Notice> noticeList1 = noticeRepository.findAllByStoreId(store.getId(), Pageable.unpaged());
    noticeService.deleteNotice(deleteNoticeForm, seller);
    Page<Notice> noticeList2 = noticeRepository.findAllByStoreId(store.getId(), Pageable.unpaged());

    //then
    assertEquals(noticeList1.getTotalElements(), 2);
    assertEquals(noticeList2.getTotalElements(), 1);
  }

  @Test
  @DisplayName("공지사항 수정 성공")
  @Transactional
  void success_updateNotice() {
    //given
    Member seller = memberRepository.save(Member.builder()
        .email("success_updateNotice@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.CUSTOMER)
        .build());

    Store store = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller)
        .build());

    Notice notice = noticeRepository.save(Notice.builder()
        .subject("제목1")
        .text("텍스트1")
        .store(store)
        .build());

    UpdateNoticeForm updateNoticeForm = UpdateNoticeForm.builder()
        .noticeId(notice.getId())
        .subject("바뀐 제목")
        .text("바뀐 텍스트")
        .build();

    //when
    noticeService.updateNotice(updateNoticeForm, seller);

    Notice updatedNotice = noticeRepository.findById(notice.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTICE));

    //then
    assertEquals(updatedNotice.getText(), updateNoticeForm.getText());
    assertEquals(updatedNotice.getSubject(), updateNoticeForm.getSubject());
  }

  @Test
  @DisplayName("가게 id로 공지사항 가져오기 성공")
  void success_getNoticeByStoreId() {
    //given
    Member seller1 = memberRepository.save(Member.builder()
        .email("success_getNoticeByStoreId1@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Member seller2 = memberRepository.save(Member.builder()
        .email("success_getNoticeByStoreId2@test.com")
        .name("강성혁")
        .password("12345678")
        .phone("010-1234-1234")
        .role(Role.SELLER)
        .build());

    Store store1 = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller1)
        .build());

    Store store2 = storeRepository.save(Store.builder()
        .storeName("강식당")
        .explains("재밌게 만들고 맛있는 음식!")
        .category(Category.DESSERT)
        .seller(seller2)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목1")
        .text("텍스트1")
        .store(store1)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목2")
        .text("텍스트1")
        .store(store1)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목1")
        .text("텍스트2")
        .store(store2)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목2")
        .text("텍스트2")
        .store(store2)
        .build());

    noticeRepository.save(Notice.builder()
        .subject("제목3")
        .text("텍스트2")
        .store(store2)
        .build());

    //when
    Page<NoticeDto> noticeDtoList1 =
        noticeService.getNoticeByStoreId(Pageable.unpaged(), store1.getId());

    Page<NoticeDto> noticeDtoList2 =
        noticeService.getNoticeByStoreId(Pageable.unpaged(), store2.getId());

    //then
    assertEquals(noticeDtoList1.getTotalElements(), 2);
    assertEquals(noticeDtoList2.getTotalElements(), 3);

  }


}