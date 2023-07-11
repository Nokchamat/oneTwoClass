package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClassBookmark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassBookmarkRepository extends JpaRepository<DayClassBookmark, Long> {

  Optional<DayClassBookmark> findByCustomerIdAndDayClassId(Long customerId, Long dayClassId);

  Page<DayClassBookmark> findAllByCustomerId(Long id, Pageable pageable);
}
