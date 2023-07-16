package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassSearchRepository extends ElasticsearchRepository<DayClassDocument, String> {

  Page<DayClassDocument> findAllByDayClassName(String dayClassNameText, Pageable pageable);

  Page<DayClassDocument> findAllByStoreId(Long storeId, Pageable pageable);
  List<DayClassDocument> findAllByStoreId(Long storeId);

  List<DayClassDocument> findAllByDayClassNameAndStoreId(String dayClassName, Long storeId);
}
