package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassBookmarkDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassBookmark;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.AddDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.DeleteDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassBookmarkRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassBookmarkService {

  private final DayClassSearchRepository dayClassSearchRepository;

  private final DayClassBookmarkRepository dayClassBookmarkRepository;

  public void addDayClassBookmark(AddDayClassBookmarkForm addDayClassBookmarkForm,
      Member customer) {

    DayClassDocument dayClassDocument =
        dayClassSearchRepository.findById(addDayClassBookmarkForm.getDayClassId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    dayClassBookmarkRepository.findByCustomerIdAndDayClassId(customer.getId(),
            dayClassDocument.getId())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_DAYCLASS_BOOKMARK);
        });

    dayClassBookmarkRepository.save(DayClassBookmark.builder()
        .dayClassId(dayClassDocument.getId())
        .customer(customer)
        .build());
  }

  public void deleteDayClassBookmark(DeleteDayClassBookmarkForm dayClassBookmarkForm,
      Member customer) {

    DayClassBookmark dayClassBookmark =
        dayClassBookmarkRepository.findById(dayClassBookmarkForm.getDayClassBookmarkId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_BOOKMARK));

    if (customer.getId() != dayClassBookmark.getCustomer().getId()) {
      throw new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_BOOKMARK);
    }

    dayClassBookmarkRepository.delete(dayClassBookmark);

  }

  public Page<DayClassBookmarkDto> getDayClassBookmark(Member customer, Pageable pageable) {

    return dayClassBookmarkRepository.findAllByCustomerId(customer.getId(), pageable)
        .map(dayClassBookmark -> {
          DayClassBookmarkDto dayClassBookmarkDto =
              DayClassBookmark.toDayClassBookmarkDto(dayClassBookmark);

          dayClassBookmarkDto.setDayClassName(
              dayClassSearchRepository.findById(dayClassBookmarkDto.getDayClassId())
                  .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS))
                  .getDayClassName());

          return dayClassBookmarkDto;
        });
  }

}
