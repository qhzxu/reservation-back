package com.reservation.reservation_server.controller.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long roomId;
    private Long userId;
    private String lastMessage;
    private long unreadCount;
    private List<ChatMessageDto> messages; // 추가
}