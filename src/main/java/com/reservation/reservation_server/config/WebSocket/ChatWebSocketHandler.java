package com.reservation.reservation_server.config.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reservation.reservation_server.common.RoleType;
import com.reservation.reservation_server.common.SenderType;
import com.reservation.reservation_server.config.Security.JwtUtil;
import com.reservation.reservation_server.entity.ChatMessage;
import com.reservation.reservation_server.entity.ChatRoom;
import com.reservation.reservation_server.repository.ChatMessageRepository;
import com.reservation.reservation_server.repository.ChatRoomRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {


    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtUtil jwtUtil;
    // 현재 연결된 세션 저장
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatWebSocketHandler(JwtUtil jwtUtil, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.jwtUtil = jwtUtil;
        this.chatMessageRepository = chatMessageRepository;
    }


    // 클라이언트가 연결될 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("연결 되었습니다");

        // HandshakeInterceptor에서 저장한 토큰 꺼내기
        String token = (String) session.getAttributes().get("jwtToken");

        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("JWT 누락"));
            return;
        }

        System.out.println("가져온 토큰: " + token);

        // 검증
        if (!jwtUtil.isValidToken(token)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("유효하지 않은 JWT"));
            return;
        }

        // userId 추출

            Long userId = jwtUtil.getUserId(token);
            String userIdStr = String.valueOf(userId);
            System.out.println("userId 추출: " + userIdStr);
            sessions.put(userIdStr, session);
            System.out.println("Connected: userId=" + userIdStr);


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode json = mapper.readTree(message.getPayload());

        Long roomId = json.get("roomId").asLong(); // 프론트에서 roomId 전달
        String content = json.get("content").asText();

        // JWT 토큰으로 권한/사용자 확인
        String token = (String) session.getAttributes().get("jwtToken");
        String role = jwtUtil.getRole(token);
        Long fromId = jwtUtil.getUserId(token);

        if (fromId == null) return;

        // 기존 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        // 메시지 DB 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSenderId(fromId);
        chatMessage.setContent(content);

        // 권한에 따라 senderType 설정
        if ("USER".equals(role)) {
            chatMessage.setSenderType(SenderType.USER);
        } else if ("STORE".equals(role)) {
            chatMessage.setSenderType(SenderType.STORE);
        } else {
            chatMessage.setSenderType(SenderType.USER); // 기본값
        }
        chatMessageRepository.save(chatMessage);

        // WebSocket 전송 (상대방이 온라인이면)
        Long toId = chatRoom.getUserId().equals(fromId) ? chatRoom.getStoreId() : chatRoom.getUserId();
        WebSocketSession receiverSession = sessions.get(String.valueOf(toId));
        ObjectNode response = mapper.createObjectNode();
        response.put("from", fromId);
        response.put("content", content);
        response.put("roomId", roomId);
        response.put("senderType", chatMessage.getSenderType().toString());

        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(response.toString()));
        }

        // 본인에게도 실시간 반영
        session.sendMessage(new TextMessage(response.toString()));
    }



    // 클라이언트 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//        System.out.println("Disconnected: " + session.getId());
        System.out.println("연결종료!!!!!!!!!!!!!!!!");
        String userId = session.getId();
        sessions.remove(userId);
    }

    // 오류 발생 시
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }
}
