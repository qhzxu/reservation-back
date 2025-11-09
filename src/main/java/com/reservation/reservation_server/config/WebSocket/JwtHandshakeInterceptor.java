package com.reservation.reservation_server.config.WebSocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        URI uri = request.getURI();
        System.out.println("uri:"+uri+"_--------------------");
        String query = uri.getQuery(); // token=eyJhbGciOi...
        if (query != null && query.startsWith("token=")) {
            String token = query.substring(6);
            attributes.put("jwtToken", token);
        }
        return true; // false면 연결 자체가 차단됨
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
