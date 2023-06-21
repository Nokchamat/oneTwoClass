package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.DayClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassRepository extends JpaRepository<DayClass, Long> {

}
