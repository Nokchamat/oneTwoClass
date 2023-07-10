package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassBookmarkDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassBookmark;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.AddDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.domain.form.dayclassbookmark.DeleteDayClassBookmarkForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassBookmarkRepository;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassBookmarkService {

  private final DayClassRepository dayClassRepository;

  private final DayClassBookmarkRepository dayClassBookmarkRepository;

  public void addDayClassBookmark(AddDayClassBookmarkForm addDayClassBookmarkForm, Member customer) {

    DayClass dayClass = dayClassRepository.findById(addDayClassBookmarkForm.getDayClassId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    dayClassBookmarkRepository.findByCustomerIdAndDayClassId(customer.getId(), dayClass.getId())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_DAYCLASS_BOOKMARK);
        });

    dayClassBookmarkRepository.save(DayClassBookmark.builder()
        .dayClass(dayClass)
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

  public List<DayClassBookmarkDto> getDayClassBookmark(Member customer, Pageable pageable) {

    return dayClassBookmarkRepository.findAllByCustomerId(customer.getId(), pageable)
        .stream().map(DayClassBookmark::toDayClassBookmarkDto).collect(Collectors.toList());
  }

}
