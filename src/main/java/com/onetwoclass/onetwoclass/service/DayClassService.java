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
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassService {

  private final StoreRepository storeRepository;

  private final DayClassRepository dayClassRepository;

  @Transactional
  public void addDayClass(AddDayClassForm addDayClassForm, Member seller) {

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
  public void updateDayClass(UpdateDayClassForm updateDayClassForm, Member seller) {

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

  public List<DayClassDto> getDayClassBySeller(Member seller, Pageable pageable) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    return dayClassRepository.findAllByStoreId(store.getId(), pageable)
        .stream().map(DayClass::toDayClassDto)
        .collect(Collectors.toList());

  }

  public void deleteDayClass(DeleteDayClassForm deleteDayClassForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClass dayclass =
        dayClassRepository.findByStoreIdAndDayClassName(
                store.getId(), deleteDayClassForm.getDayClassName())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    dayClassRepository.delete(dayclass);
  }

  public List<DayClassDto> getAllDayClass(Pageable pageable) {
    return dayClassRepository.findAll(pageable).stream()
        .map(DayClass::toDayClassDto).collect(Collectors.toList());
  }

  public List<DayClassDto> getAllDayClassByDayClassName(String dayClassName, Pageable pageable) {
    return dayClassRepository.findAllByDayClassNameContaining(dayClassName, pageable)
        .stream().map(DayClass::toDayClassDto).collect(Collectors.toList());
  }

  public List<DayClassDto> getAllDayClassByStoreId(Long storeId, Pageable pageable) {
    return dayClassRepository.findAllByStoreId(storeId, pageable)
        .stream().map(DayClass::toDayClassDto).collect(Collectors.toList());
  }

}
