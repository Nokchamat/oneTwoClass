package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassSchedulerDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclassscheduler.AddDayClassSchedulerForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassSchedulerService {

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  private final StoreRepository storeRepository;

  private final DayClassRepository dayClassRepository;

  public void addDayClassScheduler(AddDayClassSchedulerForm addScheduleForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClass dayClass = dayClassRepository
        .findById(addScheduleForm.getDayClassId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClass.getStore().getId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    dayClassSchedulerRepository
        .findByDayClassIdAndScheduledDate(dayClass.getId(), addScheduleForm.getScheduledDate())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_DAYCLASS_SCHEDULER);
        });

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .dayClass(dayClass)
        .scheduledDate(addScheduleForm.getScheduledDate())
        .build());

  }

  public List<DayClassSchedulerDto> getDayClassSchedulerByDayClassIdAndEmail(Long dayClassId,
      Member seller, Pageable pageable) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClass dayClass = dayClassRepository.findById(dayClassId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (store.getId() != dayClass.getStore().getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    List<DayClassScheduler> dayClassSchedulerList
        = dayClassSchedulerRepository.findAllByDayClassId(dayClass.getId(), pageable);

    return dayClassSchedulerList.stream()
        .map(DayClassScheduler::toDayClassSchedulerDto)
        .collect(Collectors.toList());
  }

  public List<DayClassSchedulerDto> getDayClassSchedulerByDayClassId(
      Long dayClassId, Pageable pageable) {

    return dayClassSchedulerRepository.findAllByDayClassId(dayClassId, pageable)
        .stream().map(DayClassScheduler::toDayClassSchedulerDto).collect(Collectors.toList());
  }

}
