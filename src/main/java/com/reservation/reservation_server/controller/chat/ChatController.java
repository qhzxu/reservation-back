package com.reservation.reservation_server.controller.chat;

import com.reservation.reservation_server.config.Security.CustomStoreDetails;
import com.reservation.reservation_server.config.Security.CustomUserDetails;
import com.reservation.reservation_server.config.Security.JwtUtil;
import com.reservation.reservation_server.dto.ChatRoomDto;
import com.reservation.reservation_server.entity.ChatMessage;
import com.reservation.reservation_server.entity.ChatRoom;
import com.reservation.reservation_server.entity.Store;
import com.reservation.reservation_server.entity.User;
import com.reservation.reservation_server.repository.ChatMessageRepository;
import com.reservation.reservation_server.repository.ChatRoomRepository;
import com.reservation.reservation_server.repository.StoreRepository;
import com.reservation.reservation_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    private final JwtUtil jwtUtil;

    @GetMapping("/user/rooms")
    public List<ChatRoomDto> getChatRooms(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
        Long storeId = 22L;


        ChatRoom room = chatRoomRepository.findByStoreIdAndUserId(storeId, userId)
                .orElseGet(() -> {
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setStoreId(storeId);
                    newRoom.setUserId(userId);
                    newRoom.setStoreId(storeId);
                    return chatRoomRepository.save(newRoom);
                });

//        ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(room.getId());
        long unreadCount = chatMessageRepository.countByChatRoomIdAndReadStatusFalse(room.getId());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        Store store = storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("상점을 찾을 수 없습니다"));

//        String lastContent = lastMessage != null ? lastMessage.getContent() : "";

        ChatRoomDto dto = new ChatRoomDto(
                room.getId(),
                room.getUserId(),
                user.getEmail(),
                user.getName(),
                null,
                unreadCount,
                room.getStoreId(),
                store.getName()
        );

        // 단일 객체도 리스트로 감싸서 반환
        return Collections.singletonList(dto);
    }



    // 2. 특정 채팅방 메시지 조회
    @GetMapping("/user/rooms/{roomId}/messages")
    public List<ChatMessageDto> getMessages(@PathVariable Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(roomId);

        if (messages.isEmpty()) return Collections.emptyList();

        for(ChatMessage message : messages) {
            System.out.println(message.getContent());
        }

        return messages.stream()
                .map(msg -> new ChatMessageDto(
                        msg.getSenderId(),
                        msg.getContent(),
                        msg.getCreatedAt(),
                        msg.getReadStatus(),
                        msg.getSenderType().toString()
                ))
                .collect(Collectors.toList());
    }

    // 1️⃣ 관리자 기준 채팅방 목록 조회
    @GetMapping("/store/rooms")
    public List<ChatRoomDto> getAdminChatRooms(@AuthenticationPrincipal CustomStoreDetails customStoreDetails) {
        Long storeId = customStoreDetails.getId(); // 관리자 ID == storeId
        List<ChatRoom> rooms = chatRoomRepository.findAllByStoreId(storeId);

        return rooms.stream().map(room -> {
            Long userId = room.getUserId();

            // 유저 정보 조회
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            // 읽지 않은 메시지 수
            long unreadCount = chatMessageRepository.countByChatRoomIdAndReadStatusFalse(room.getId());

            // 최근 메시지 (필요 시)
            // ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(room.getId());
            // String lastContent = lastMessage != null ? lastMessage.getContent() : null;

            // DTO 생성
            return new ChatRoomDto(
                    room.getId(),
                    user.getUserId(),
                    user.getEmail(),
                    user.getName(),
                    null, // lastMessage 자리
                    unreadCount,
                    room.getStoreId(),
                    null
            );
        }).collect(Collectors.toList());
    }


    // 2️⃣ 특정 채팅방 메시지 조회 (관리자)
    @GetMapping("/store/rooms/{roomId}/messages")
    public List<ChatMessageDto> getAdminMessages(@PathVariable Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(roomId);

        return messages.stream()
                .map(msg -> new ChatMessageDto(
                        msg.getSenderId(),
                        msg.getContent(),
                        msg.getCreatedAt(),
                        msg.getReadStatus(),
                        msg.getSenderType().toString()
                ))
                .collect(Collectors.toList());
    }
}
