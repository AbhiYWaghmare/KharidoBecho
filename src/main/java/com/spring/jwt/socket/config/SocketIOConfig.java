////************ WithOut Annotation Use this **************//
//
//
//package com.spring.jwt.socket.config;
//
//import com.corundumstudio.socketio.SocketIOServer;
//import jakarta.annotation.PreDestroy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SocketIOConfig {
//
//    @Bean
//    public SocketIOServer socketIOServer() {
//
//        com.corundumstudio.socketio.Configuration socketConfig =
//                new com.corundumstudio.socketio.Configuration();
//
//        socketConfig.setHostname("0.0.0.0");
//        socketConfig.setPort(9092);
//
//        // Allow frontend & APK
//        socketConfig.setOrigin("*");
//
//        return new SocketIOServer(socketConfig);
//    }
//}


//******************** With Annotation*********************************//

//package com.spring.jwt.socket.config;
//
//import com.corundumstudio.socketio.SocketIOServer;
//import jakarta.annotation.PreDestroy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SocketIOConfig {
//
//    private SocketIOServer server;
//
//    @Bean
//    public SocketIOServer socketIOServer() {
//
//        // ✅ FULLY QUALIFIED NAME (THIS IS THE KEY)
//        com.corundumstudio.socketio.Configuration socketConfig =
//                new com.corundumstudio.socketio.Configuration();
//
//        socketConfig.setHostname("localhost");
//        socketConfig.setPort(9092);
//
//        // REQUIRED for @OnConnect / @OnEvent
//        socketConfig.setAnnotationScannerPackage(
//                "com.spring.jwt.socket.handler"
//        );
//
//        server = new SocketIOServer(socketConfig);
//        server.start();
//
//        System.out.println("🚀 Socket.IO server started on port 9092");
//
//        return server;
//    }
//
//    @PreDestroy
//    public void stop() {
//        if (server != null) {
//            server.stop();
//            System.out.println("🛑 Socket.IO server stopped");
//        }
//    }
//}
