package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  Optional<Schedule> findByCustomerIdAndDayClassSchedulerIdAndRequestedDateTime(Long customerId, Long dayClassSchedulerId, LocalDateTime requestedDateTime);
  List<Schedule> findAllByCustomerId(Long customerId, Pageable pageable);
  List<Schedule> findAllByDayClassSchedulerId(Long id, Pageable pageable);
}
