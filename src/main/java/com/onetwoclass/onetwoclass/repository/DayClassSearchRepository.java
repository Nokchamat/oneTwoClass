package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClassDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassSearchRepository extends ElasticsearchRepository<DayClassDocument, String> {

  Page<DayClassDocument> findAllByDayClassNameText(String dayClassName, Pageable pageable);

  Page<DayClassDocument> findAllByStoreId(Long storeId, Pageable pageable);
  List<DayClassDocument> findAllByStoreId(Long storeId);

  Optional<DayClassDocument> findByStoreIdAndDayClassNameKeyword(Long storeId, String dayClassName);

}
