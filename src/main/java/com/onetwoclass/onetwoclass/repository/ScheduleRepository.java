package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
