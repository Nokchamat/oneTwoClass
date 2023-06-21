package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.StoreBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreBookmarkRepository extends JpaRepository<StoreBookmark, Long> {

}
