package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.DayClassScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassSchedulerRepository extends JpaRepository<DayClassScheduler, Long> {

}
