package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassSchedulerDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclassscheduler.AddDayClassSchedulerForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassSchedulerService {

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  private final StoreRepository storeRepository;

  private final DayClassSearchRepository dayClassSearchRepository;

  public void addDayClassScheduler(AddDayClassSchedulerForm addScheduleForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClassDocument dayClass = dayClassSearchRepository
        .findById(addScheduleForm.getDayClassId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClass.getStoreId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    dayClassSchedulerRepository
        .findByDayClassIdAndScheduledDate(dayClass.getId(), addScheduleForm.getScheduledDate())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_DAYCLASS_SCHEDULER);
        });

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .dayClassId(dayClass.getId())
        .scheduledDate(addScheduleForm.getScheduledDate())
        .build());

  }

  public Page<DayClassSchedulerDto> getDayClassSchedulerByDayClassIdAndEmail(String dayClassId,
      Member seller, Pageable pageable) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClassDocument dayClassDocument = dayClassSearchRepository.findById(dayClassId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (store.getId() != dayClassDocument.getStoreId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    return dayClassSchedulerRepository.findAllByDayClassId(dayClassDocument.getId(), pageable)
        .map(DayClassScheduler::toDayClassSchedulerDto);
  }

  public Page<DayClassSchedulerDto> getDayClassSchedulerByDayClassId(
      String dayClassId, Pageable pageable) {

    return dayClassSchedulerRepository.findAllByDayClassId(dayClassId, pageable)
        .map(DayClassScheduler::toDayClassSchedulerDto);
  }

}
