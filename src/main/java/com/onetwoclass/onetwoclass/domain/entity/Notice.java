package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.NoticeDto;
import com.onetwoclass.onetwoclass.domain.form.notice.UpdateNoticeForm;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Notice extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String subject;

  @Lob
  private String text;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  public void updateNotice(UpdateNoticeForm updateNoticeForm) {

    if (updateNoticeForm.getSubject() != null) {
      this.subject = updateNoticeForm.getSubject();
    }

    if (updateNoticeForm.getText() != null) {
      this.text = updateNoticeForm.getText();
    }

  }

  public static NoticeDto toNoticeDto(Notice notice) {

    return NoticeDto.builder()
        .id(notice.getId())
        .subject(notice.getSubject())
        .text(notice.getText())
        .build();

  }

}
