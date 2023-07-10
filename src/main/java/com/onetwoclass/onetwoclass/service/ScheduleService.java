package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.ScheduleDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.schedule.RequestScheduleForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.DayClassSchedulerRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.ScheduleRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;

  private final MemberRepository memberRepository;

  private final StoreRepository storeRepository;

  private final DayClassRepository dayClassRepository;

  private final DayClassSchedulerRepository dayClassSchedulerRepository;

  public void requestSchedule(RequestScheduleForm requestScheduleForm, String email) {

    Member customer = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

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

  public List<ScheduleDto> getAllScheduleByCustomerEmail(String email, Pageable pageable) {

    Member customer = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    return scheduleRepository.findAllByCustomerId(customer.getId(), pageable)
        .stream().map(Schedule::toScheduleDto).collect(Collectors.toList());
  }

  public List<ScheduleDto> getAllScheduleBySellerEmailAndDayClassSchedulerId(
      String email, Long dayClassSchedulerId, Pageable pageable) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClassScheduler dayClassScheduler = dayClassSchedulerRepository.findById(dayClassSchedulerId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_SCHEDULER));

    DayClass dayClass = dayClassRepository.findById(dayClassScheduler.getDayClass().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClass.getStore().getId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    return scheduleRepository.findAllByDayClassSchedulerId(
            dayClassScheduler.getId(), pageable)
        .stream().map(Schedule::toScheduleDto).collect(Collectors.toList());
  }

  @Transactional
  public void acceptScheduleRequest(String email, Long scheduleId) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    Schedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    DayClassScheduler dayClassScheduler =
        dayClassSchedulerRepository.findById(schedule.getDayClassScheduler().getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS_SCHEDULER));

    DayClass dayClass = dayClassRepository.findById(dayClassScheduler.getDayClass().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClass.getStore().getId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    if (schedule.getAcceptYn()) {
      throw new CustomException(ErrorCode.ALREADY_ACCEPTED_SCHEDULE);
    }

    schedule.acceptRequestSchedule();
  }


}
