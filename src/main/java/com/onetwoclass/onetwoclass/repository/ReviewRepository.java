package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  Optional<Review> findByScheduleId(Long scheduleId);
  List<Review> findAllByCustomerId(Long id, Pageable pageable);
  List<Review> findAllByDayClassId(Long id, Pageable pageable);
}
