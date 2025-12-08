package com.spring.jwt.socketio;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.spring.jwt.laptop.laptopAuction.laptopSocket.handler.ChatSocketHandler;
import com.spring.jwt.laptop.service.LaptopRequestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${socketio.host:0.0.0.0}")
    private String host;

    @Value("${socketio.port:3000}")
    private Integer port;

    @Value("${socketio.bossCount:1}")
    private Integer bossCount;

    @Value("${socketio.workCount:4}")
    private Integer workCount;

    @Value("${socketio.pingTimeOut:60000}")
    private Integer pingTimeOut;

    @Value("${socketio.pingInterval:25000}")
    private Integer pingInterval;


    @Bean
    public SocketIOServer socketIOServer(){
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setReuseAddress(true);

        com.corundumstudio.socketio.Configuration config =
                new com.corundumstudio.socketio.Configuration();

        config.setHostname(host);
        config.setPort(port);
        config.setSocketConfig(socketConfig);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setPingTimeout(pingTimeOut);
        config.setPingInterval(pingInterval);
        config.setOrigin("*"); // allow all frontend origins

        return new SocketIOServer(config);
    }

    @Bean
    public ChatSocketHandler chatSocketHandler(SocketIOServer server,
                                               LaptopRequestService laptopRequestService) {
        return new ChatSocketHandler(server, laptopRequestService);
    }
//
//    @Bean
//    public CommandLineRunner runner(SocketIOServer server) {
//        return args -> {
//            server.start();
//            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
//        };
//    }
}
