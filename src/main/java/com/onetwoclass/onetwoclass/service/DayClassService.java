package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.domain.dto.DayClassDto;
import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import com.onetwoclass.onetwoclass.domain.entity.Member;
import com.onetwoclass.onetwoclass.domain.entity.Store;
import com.onetwoclass.onetwoclass.domain.form.dayclass.AddDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.DeleteDayClassForm;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import com.onetwoclass.onetwoclass.exception.CustomException;
import com.onetwoclass.onetwoclass.exception.ErrorCode;
import com.onetwoclass.onetwoclass.repository.DayClassSearchRepository;
import com.onetwoclass.onetwoclass.repository.StoreRepository;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassService {

  private final StoreRepository storeRepository;

  private final DayClassSearchRepository dayClassSearchRepository;

  private final ReviewService reviewService;

  @Transactional
  public void addDayClass(AddDayClassForm addDayClassForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    dayClassSearchRepository
        .findByStoreIdAndDayClassNameKeyword(store.getId(), addDayClassForm.getDayClassName())
        .ifPresent(d -> {
          throw new CustomException(ErrorCode.DUPLICATION_DAYCLASS_NAME);
        });

    dayClassSearchRepository.save(DayClassDocument.builder()
        .dayClassNameText(addDayClassForm.getDayClassName())
        .explains(addDayClassForm.getExplains())
        .price(addDayClassForm.getPrice())
        .storeId(store.getId())
        .modifiedAt(LocalDateTime.now())
        .registeredAt(LocalDateTime.now())
        .build());

  }


  @Transactional
  public void updateDayClass(UpdateDayClassForm updateDayClassForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    dayClassSearchRepository.findByStoreIdAndDayClassNameKeyword(
            store.getId(), updateDayClassForm.getToChangeDayClassName())
        .ifPresent(a -> {
          throw new CustomException(ErrorCode.DUPLICATION_DAYCLASS_NAME);
        });

    DayClassDocument dayClassDocument = dayClassSearchRepository
        .findById(updateDayClassForm.getDayClassId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (dayClassDocument.getStoreId() != store.getId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    dayClassDocument.updateDayClass(updateDayClassForm);

    dayClassSearchRepository.save(dayClassDocument);

  }

  public Page<DayClassDto> getDayClassBySeller(Member seller, Pageable pageable) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    return dayClassSearchRepository.findAllByStoreId(store.getId(), pageable)
        .map(dayClass -> {
          DayClassDto dayClassDto = DayClassDocument.toDayClassDto(dayClass);
          dayClassDto.setStar(reviewService.getDayClassStarScore(dayClassDto.getDayClassId()));

          return dayClassDto;
        });
  }

  public void deleteDayClass(DeleteDayClassForm deleteDayClassForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    DayClassDocument dayClassDocument
        = dayClassSearchRepository.findById(deleteDayClassForm.getDayClassId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS));

    if (store.getId() != dayClassDocument.getStoreId()) {
      throw new CustomException(ErrorCode.MISMATCHED_SELLER_AND_DAYCLASS);
    }

    dayClassSearchRepository.delete(dayClassDocument);
  }

  public Page<DayClassDto> getAllDayClassByStoreId(Long storeId, Pageable pageable) {
    return dayClassSearchRepository.findAllByStoreId(storeId, pageable)
        .map(dayClass -> {
          DayClassDto dayClassDto = DayClassDocument.toDayClassDto(dayClass);
          dayClassDto.setStar(reviewService.getDayClassStarScore(String.valueOf(dayClass.getId())));

          return dayClassDto;
        });
  }

  public Page<DayClassDto> getAllDayClassByDayClassNameFromElasticsearch(String dayClassname,
      Pageable pageable) {

    return dayClassSearchRepository.findAllByDayClassNameText(dayClassname, pageable)
        .map(dayClass -> {
          DayClassDto dayClassDto = DayClassDocument.toDayClassDto(dayClass);
          dayClassDto.setStar(reviewService.getDayClassStarScore(dayClassDto.getDayClassId()));

          return dayClassDto;
        });
  }

  public Page<DayClassDto> getAllDayClassFromElasticsearch(Pageable pageable) {

    return dayClassSearchRepository.findAll(pageable).map(dayClass -> {
      DayClassDto dayClassDto = DayClassDocument.toDayClassDto(dayClass);
      dayClassDto.setStar(reviewService.getDayClassStarScore(dayClassDto.getDayClassId()));

      return dayClassDto;
    });
  }

  public DayClassDto getDayClassDocumentFromElasticsearch(String dayClassId) {

    DayClassDto dayClassDto = DayClassDocument.toDayClassDto(
        dayClassSearchRepository.findById(dayClassId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS)));

    dayClassDto.setStar(reviewService.getDayClassStarScore(dayClassDto.getDayClassId()));

    return dayClassDto;
  }

}
