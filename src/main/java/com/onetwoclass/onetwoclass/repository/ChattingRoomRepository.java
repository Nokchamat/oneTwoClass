package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.ChattingRoom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  Optional<ChattingRoom> findByCustomerIdAndSellerIdAndExitCustomerYnIsFalse(Long customerId,
      Long sellerId);

  Page<ChattingRoom> findAllByCustomerIdAndExitCustomerYnIsFalse(Long id, Pageable pageable);

  Page<ChattingRoom> findAllBySellerIdAndExitSellerYnIsFalse(Long id, Pageable pageable);
}
