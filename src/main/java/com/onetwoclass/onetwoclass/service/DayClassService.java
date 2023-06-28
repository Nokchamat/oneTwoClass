package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclass.AddDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.DeleteDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassRepository;
import com.onetwoclass.onetwoclass.repository.MemberRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassService {

  private final MemberRepository memberRepository;

  private final StoreRepository storeRepository;

  private final DayClassRepository dayClassRepository;

  @Transactional
  public void addDayClass(AddDayClassForm addDayClassForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    dayClassRepository
        .findByStoreIdAndDayClassName(store.getId(), addDayClassForm.getDayClassName())
        .ifPresent(d -> {
          throw new CustomException(ErrorCode.DUPLICATION_DAYCLASS_NAME);
        });

    dayClassRepository.save(DayClass.builder()
        .dayClassName(addDayClassForm.getDayClassName())
        .explains(addDayClassForm.getExplains())
        .price(addDayClassForm.getPrice())
        .store(store)
        .build());

  }


  @Transactional
  public void updateDayClass(UpdateDayClassForm updateDayClassForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    dayClassRepository.findByStoreIdAndDayClassName(
            store.getId(), updateDayClassForm.getToChangeDayClassName())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.DUPLICATION_DAYCLASS_NAME);
        });

    DayClass dayClass = dayClassRepository
        .findByStoreIdAndDayClassName(store.getId(), updateDayClassForm.getDayClassName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    dayClass.updateDayClass(updateDayClassForm);

  }

  public List<DayClassDto> getDayClassByEmail(String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    return dayClassRepository.findByStoreId(store.getId())
        .stream().map(DayClass::toDayClassDto)
        .collect(Collectors.toList());

  }

  public void deleteDayClass(DeleteDayClassForm deleteDayClassForm, String email) {

    Member seller = memberRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClass dayclass =
        dayClassRepository.findByStoreIdAndDayClassName(
                store.getId(), deleteDayClassForm.getDayClassName())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    dayClassRepository.delete(dayclass);
  }

}
