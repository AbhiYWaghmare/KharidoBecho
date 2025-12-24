<<<<<<< HEAD

<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/carsocket/JwtHandshakeInterceptor.java
package com.spring.jwt.car.auction.carsocket;
=======
=======
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
package com.spring.jwt.socket;
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/socket/JwtHandshakeInterceptor.java

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
<<<<<<< HEAD
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
=======
public class  JwtHandshakeInterceptor implements HandshakeInterceptor {
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
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
