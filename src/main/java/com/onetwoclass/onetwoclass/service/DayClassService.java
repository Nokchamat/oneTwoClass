package com.onetwoclass.onetwoclass.service;

import com.onetwoclass.onetwoclass.config.elasticsearch.DayClassDocument;
import com.onetwoclass.onetwoclass.config.elasticsearch.DayClassSearchRepository;
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
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayClassService {

  private final StoreRepository storeRepository;

  private final DayClassRepository dayClassRepository;

  private final ReviewService reviewService;

  private final DayClassSearchRepository dayClassSearchRepository;

  @Transactional
  public void addDayClass(AddDayClassForm addDayClassForm, Member seller) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    dayClassRepository
        .findByStoreIdAndDayClassName(store.getId(), addDayClassForm.getDayClassName())
        .ifPresent(d -> {
          throw new CustomException(ErrorCode.DUPLICATION_DAYCLASS_NAME);
        });

    DayClass dayClass = dayClassRepository.save(DayClass.builder()
        .dayClassName(addDayClassForm.getDayClassName())
        .explains(addDayClassForm.getExplains())
        .price(addDayClassForm.getPrice())
        .store(store)
        .build());

    dayClassSearchRepository.save(DayClassDocument.builder()
        .id(dayClass.getId())
        .dayClassName(addDayClassForm.getDayClassName())
        .explains(addDayClassForm.getExplains())
        .price(addDayClassForm.getPrice())
        .storeId(store.getId())
        .modifiedAt(dayClass.getModifiedAt())
        .registeredAt(dayClass.getRegisteredAt())
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

  public Page<DayClassDto> getDayClassBySeller(Member seller, Pageable pageable) {

    Store store = storeRepository.findBySellerId(seller.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

    return dayClassRepository.findAllByStoreId(store.getId(), pageable)
        .map(dayClass -> {
          DayClassDto dayClassDto = DayClass.toDayClassDto(dayClass);
          dayClassDto.setStar(reviewService.getDayClassStarScore(dayClass.getId()));

          return dayClassDto;
        });
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

  public Page<DayClassDto> getAllDayClass(Pageable pageable) {
    return dayClassRepository.findAll(pageable).map(dayClass -> {
      DayClassDto dayClassDto = DayClass.toDayClassDto(dayClass);
      dayClassDto.setStar(reviewService.getDayClassStarScore(dayClass.getId()));

      return dayClassDto;
    });
  }

  public Page<DayClassDto> getAllDayClassByStoreId(Long storeId, Pageable pageable) {
    return dayClassRepository.findAllByStoreId(storeId, pageable)
        .map(dayClass -> {
          DayClassDto dayClassDto = DayClass.toDayClassDto(dayClass);
          dayClassDto.setStar(reviewService.getDayClassStarScore(dayClass.getId()));

          return dayClassDto;
        });
  }

  public Page<DayClassDocument> getAllDayClassByDayClassNameFromElasticsearch(String dayClassname,
      Pageable pageable) {

    return dayClassSearchRepository.findAllByDayClassName(dayClassname, pageable);
  }

  public Page<DayClassDto> getAllDayClassEsAll(Pageable pageable) {

    return dayClassSearchRepository.findAll(pageable).map(DayClassDocument::toDayClassDto);
  }

  public DayClassDto getDayClassDocumentById(Long dayClassId) {

    return DayClassDocument.toDayClassDto(dayClassSearchRepository.findById(dayClassId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DAYCLASS)));
  }

}
