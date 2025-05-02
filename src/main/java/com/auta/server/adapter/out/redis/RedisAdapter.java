package com.auta.server.adapter.out.redis;

import com.auta.server.application.port.out.auth.RefreshTokenStorePort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisAdapter implements RefreshTokenStorePort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void store(String key, String token, long expirationMillis) {
        redisTemplate.opsForValue().set(
                key, token, expirationMillis, TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
