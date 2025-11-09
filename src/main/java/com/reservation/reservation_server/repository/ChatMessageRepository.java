package com.reservation.reservation_server.repository;

import com.reservation.reservation_server.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    long countByChatRoomIdAndReadStatusFalse(Long id);
    List<ChatMessage> findAllByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}