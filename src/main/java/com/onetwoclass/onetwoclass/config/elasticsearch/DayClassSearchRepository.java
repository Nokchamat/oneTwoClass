package com.onetwoclass.onetwoclass.config.elasticsearch;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassSearchRepository extends ElasticsearchRepository<DayClassDocument, Long> {

  Page<DayClassDocument> findAllByDayClassName(String dayClassName, Pageable pageable);

  @Override
  Optional<DayClassDocument> findById(Long id);
}
