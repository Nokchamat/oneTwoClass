package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.DayClassBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayClassBookmarkRepository extends JpaRepository<DayClassBookmark, Long> {

}
