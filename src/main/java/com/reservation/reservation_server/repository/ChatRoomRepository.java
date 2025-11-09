package com.reservation.reservation_server.repository;

import com.reservation.reservation_server.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByUserIdAndStoreId(Long aLong, Long aLong1);

    List<ChatRoom> findAllByStoreId(Long storeId);

    List<ChatRoom> findAllByStoreIdAndUserId(Long storeId, Long userId);

    Optional<ChatRoom> findByStoreIdAndUserId(Long storeId, Long userId);
}
