package com.babyak.babyak.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public void setRedisRefreshToken(String email, String refreshToken) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(email, refreshToken, Duration.ofDays(30));
    }

    public String getRedisRefreshToken(String key) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        return stringValueOperations.get(key);
    }

    public void setRedisLogoutAccTkn(String accessToken, Long expiration) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(accessToken, "logout", Duration.ofMillis(expiration));
    }

    public String getRedisLogoutAccTkn(String accessToken) {
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        return stringValueOperations.get(accessToken);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

}
