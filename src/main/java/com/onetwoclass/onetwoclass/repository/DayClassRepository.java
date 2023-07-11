package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassRepository extends JpaRepository<DayClass, Long> {

  Optional<DayClass> findByStoreIdAndDayClassName(Long id, String dayClassName);

  Page<DayClass> findAllByStoreId(Long id, Pageable pageable);

  List<DayClass> findAllByStoreId(Long id);

  Page<DayClass> findAllByDayClassNameContaining(String dayClassName, Pageable pageable);

}
