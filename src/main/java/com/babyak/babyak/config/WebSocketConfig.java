package com.babyak.babyak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * [Socket 연결 endpoint]
         * /ws-stomp 를 endpoint로 설정 -> 핸드쉐이크 통해 커넥션 생성
         */
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * [message 구독 요청: SUBSCRIBE]
         * ex) /sub/chat/room/{roomId}
         */
        registry.enableSimpleBroker("/sub");
        /**
         * [message 발행 요청: PUBLISH]
         * ex) /pub/chat/message
         */
        registry.setApplicationDestinationPrefixes("/pub");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }
}
