package com.reservation.reservation_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    private Long roomId;          // ChatRoom ID
    private Long userId;          // 상대방 유저 ID
    private String userEmail;
    private String userName;
    private String lastMessage;   // 마지막 메시지 내용
    private long unreadCount;     // 안 읽은 메시지 수
    private Long storeId;
}
