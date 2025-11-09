package com.reservation.reservation_server.controller.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long senderId;        // 메시지 보낸 사람(userId 또는 storeId)
    private String content;       // 메시지 내용
    private LocalDateTime createdAt;  // 메시지 전송 시간
    private Boolean readStatus;   // 읽음 여부 (선택사항)
    private String senderType;
}
