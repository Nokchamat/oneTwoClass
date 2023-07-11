package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.StoreBookmark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {

  Page<StoreBookmark> findAllByCustomerId(Long customerId, Pageable pageable);

  Optional<StoreBookmark> findByCustomerIdAndStoreId(Long customerId, Long storeId);
}
