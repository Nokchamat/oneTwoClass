package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassRepository extends JpaRepository<DayClass, Long> {

  Optional<List<DayClass>> findByStoreId(Long id);
}
