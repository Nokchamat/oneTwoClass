package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.ChattingRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  Optional<ChattingRoom> findByCustomerIdAndSellerIdAndExitCustomerYnIsFalse(Long customerId, Long sellerId);

  List<ChattingRoom> findAllByCustomerIdAndExitCustomerYnIsFalse(Long id, Pageable pageable);
  List<ChattingRoom> findAllBySellerIdAndExitSellerYnIsFalse(Long id, Pageable pageable);
}
