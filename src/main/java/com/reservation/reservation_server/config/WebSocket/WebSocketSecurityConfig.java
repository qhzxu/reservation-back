//package com.reservation.reservation_server.config.WebSocket;
//
//import com.reservation.reservation_server.config.Security.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws/chat")
////                .setAllowedOriginPatterns("*")
////                .withSockJS();
////    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String token = (String) accessor.getSessionAttributes().get("jwtToken");
//                    System.out.println(token + "_-----------------------");
//
//                    if (token == null || token.isEmpty()) {
//                        throw new IllegalArgumentException("JWT 누락");
//                    }
//
//                    if (!validateToken(token)) {
//                        throw new IllegalArgumentException("JWT 유효하지 않음");
//                    }
//
//                    System.out.println("JWT 인증 성공: " + token);
//                }
//
//                return message;
//            }
//        });
//    }
//
//
////    @Override
////    public void configureClientInboundChannel(ChannelRegistration registration) {
////        registration.interceptors(new ChannelInterceptor() {
////            @Override
////            public Message<?> preSend(Message<?> message, MessageChannel channel) {
////                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
////                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
////                    String token = accessor.getFirstNativeHeader("Authorization");
////                    // JWT 검증 로직 추가
////                    if (token == null || !validateToken(token)) {
////                        throw new IllegalArgumentException("No valid token");
////                    }
////                }
////                return message;
////            }
////        });
////    }
////
//    private boolean validateToken(String token) {
//    try {
//        return jwtUtil.isValidToken(token);
//    } catch (Exception e) {
//        System.out.println("JWT 검증 실패: " + e.getMessage());
//        return false;
//    }
//}
//}
