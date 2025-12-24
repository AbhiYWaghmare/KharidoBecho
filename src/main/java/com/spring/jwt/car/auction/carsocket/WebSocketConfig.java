<<<<<<< HEAD

<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/carsocket/WebSocketConfig.java
package com.spring.jwt.car.auction.carsocket;
=======
=======
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
package com.spring.jwt.socket;
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/socket/WebSocketConfig.java

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

<<<<<<< HEAD

=======
//    Use this when we enable authentication

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws-auction")
//                .setAllowedOriginPatterns("*")
//                .addInterceptors(jwtHandshakeInterceptor)   // use injected bean
//                .withSockJS();
//    }
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-auction")
                .setAllowedOriginPatterns("*")
                .withSockJS();  //  no interceptor
    }

}
