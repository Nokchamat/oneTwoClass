package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClassScheduler;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassSchedulerRepository extends JpaRepository<DayClassScheduler, Long> {

  Optional<DayClassScheduler> findByDayClassIdAndScheduledDate(String dayClassId,
      LocalDateTime scheduledDate);

  Page<DayClassScheduler> findAllByDayClassId(String dayClassId, Pageable pageable);
}
