package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

}
