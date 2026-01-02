
package com.spring.jwt.car.auction.carsocket;

import com.spring.jwt.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class CarJwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    public CarJwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request,
//                                   WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) {
//        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
//            return true;
//        }
//
//        HttpServletRequest servletReq = servletRequest.getServletRequest();
//
//        // 1) Query param ?access_token=
//        String token = servletReq.getParameter("access_token");
//
//        // 2) Fallback to Authorization header
//        if (token == null) {
//            String auth = servletReq.getHeader("Authorization");
//            if (auth != null && auth.startsWith("Bearer ")) {
//                token = auth.substring(7);
//            }
//        }
//
//        if (token == null || token.isEmpty()) {
//            // If you want to force auth, return false here
//            return true;
//        }
//
//        try {
//            Long userId = jwtService.extractUserId(token);
//            if (userId != null) {
//                attributes.put("userId", userId);
//                return true;
//            } else {
//                return false; // no userId in token
//            }
//        } catch (Exception e) {
//            return false; // invalid token → reject
//        }
//    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return true;
        }

        HttpServletRequest servletReq = servletRequest.getServletRequest();

        // 1) Query param ?access_token=
        String token = servletReq.getParameter("access_token");

        // 2) Fallback to Authorization header
        if (token == null) {
            String auth = servletReq.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
            }
        }

        if (token == null || token.isEmpty()) {
            // If you want to force auth, return false here
            return true;
        }

        try {
            Long userId = jwtService.extractUserId(token);
            if (userId != null) {
                attributes.put("userId", userId);
                return true;
            } else {
                return false; // no userId in token
            }
        } catch (Exception e) {
            return false; // invalid token → reject
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // optional logging
    }
}
