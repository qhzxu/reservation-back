package com.reservation.reservation_server.entity;

import com.reservation.reservation_server.common.SenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 채팅방의 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    // 메시지 보낸 사람 userId (유저 또는 스토어)
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    // 메시지 내용
    @Column(name = "content", nullable = false)
    private String content;

    // 메시지 보낸 시간
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 읽음 여부 (0: 안읽음, 1: 읽음)
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;

    @Column(name = "sender_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SenderType senderType;

}
