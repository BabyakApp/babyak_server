package com.babyak.babyak.component;

import com.babyak.babyak.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    // WebSocket 통해 들어온 요청 처리 전 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // WebSocket 연결 시 헤더의 jwt 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String header = accessor.getFirstNativeHeader("Authorization");
            jwtTokenProvider.validateToken(header.substring(7));
        }
        return message;
    }
}
