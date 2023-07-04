package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassSchedulerDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.schedule.AddDayClassScheduler;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassSchedulerService {

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  private final MemberRepository memberRepository;

  private final StoreRepository storeRepository;

  private final DayClassRepository dayClassRepository;

  public void addDayClassScheduler(AddDayClassScheduler addScheduleForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClass dayClass = dayClassRepository
        .findByStoreIdAndDayClassName(store.getId(), addScheduleForm.getDayClassName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    dayClassSchedulerRepository
        .findByDayClassIdAndScheduledDate(dayClass.getId(), addScheduleForm.getScheduledDate())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_EXIST_SCHEDULER);
        });

    dayClassSchedulerRepository.save(DayClassScheduler.builder()
        .dayClass(dayClass)
        .scheduledDate(addScheduleForm.getScheduledDate())
        .build());

  }

  public List<DayClassSchedulerDto> getDayClassSchedulerBySellerEmailAndName(String dayClassName,
      String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClass dayClass = dayClassRepository
        .findByStoreIdAndDayClassName(store.getId(), dayClassName)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    List<DayClassScheduler> dayClassSchedulerList
        = dayClassSchedulerRepository.findAllByDayClassId(dayClass.getId());

    return dayClassSchedulerList.stream()
        .map(DayClassScheduler::toDayClassSchedulerDto)
        .collect(Collectors.toList());
  }

  public List<DayClassSchedulerDto> getDayClassSchedulerByDayClassId(Long dayClassId) {

    return dayClassSchedulerRepository.findAllByDayClassId(dayClassId)
        .stream().map(DayClassScheduler::toDayClassSchedulerDto).collect(Collectors.toList());
  }

}
