package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  Optional<Schedule> findByCustomerIdAndDayClassSchedulerId(Long customerId, Long dayClassSchedulerId);
  List<Schedule> findAllByCustomerId(Long customerId, Pageable pageable);
  List<Schedule> findAllByDayClassSchedulerId(Long id, Pageable pageable);
}
