package com.onetwoclass.onetwoclass.repository;

import com.onetwoclass.onetwoclass.domain.entity.Chatting;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {

  void deleteAllByChattingRoomId(Long id);

  List<Chatting> findAllByChattingRoomId(Long chattingRoomId, Pageable pageable);
}
