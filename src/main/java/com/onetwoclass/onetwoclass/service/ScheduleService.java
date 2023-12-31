package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.ScheduleDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.schedule.RequestScheduleForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import com.onetwoclass.onetwoclass.repository.ScheduleRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;

  private final StoreRepository storeRepository;

  private final DayClassSearchRepository dayClassSearchRepository;

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  public void requestSchedule(RequestScheduleForm requestScheduleForm, Member customer) {

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.findById(requestScheduleForm.getDayClassSchedulerId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    scheduleRepository.findByCustomerIdAndDayClassSchedulerId(
            customer.getId(), requestScheduleForm.getDayClassSchedulerId())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.ALREADY_REQUESTED_SCHEDULE);
        });

    scheduleRepository.save(Schedule.builder()
        .dayClassScheduler(dayClassScheduler)
        .requestedDateTime(dayClassScheduler.getScheduledDate())
        .customer(customer)
        .acceptYn(false)
        .build());

  }

  public Page<ScheduleDto> getAllScheduleByCustomerEmail(Member customer, Pageable pageable) {

    return scheduleRepository.findAllByCustomerId(customer.getId(), pageable)
        .map(Schedule::toScheduleDto);
  }

  public Page<ScheduleDto> getAllScheduleBySellerEmailAndDayClassSchedulerId(
      Member seller, Long dayClassSchedulerId, Pageable pageable) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClassScheduler dayClassScheduler = dayClassSchedulerRepository.findById(dayClassSchedulerId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_SCHEDULER));

    DayClassDocument dayClassDocument =
        dayClassSearchRepository.findById(dayClassScheduler.getDayClassId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClassDocument.getStoreId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    return scheduleRepository.findAllByDayClassSchedulerId(
        dayClassScheduler.getId(), pageable).map(Schedule::toScheduleDto);
  }

  @Transactional
  public void acceptScheduleRequest(Member seller, Long scheduleId) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    Schedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.findById(schedule.getDayClassScheduler().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_SCHEDULER));

    DayClassDocument dayClassDocument =
        dayClassSearchRepository.findById(dayClassScheduler.getDayClassId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClassDocument.getStoreId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    if (schedule.getAcceptYn()) {
      throw new CustomException(ErrorCode.ALREADY_ACCEPTED_SCHEDULE);
    }

    schedule.acceptRequestSchedule();
  }


}
