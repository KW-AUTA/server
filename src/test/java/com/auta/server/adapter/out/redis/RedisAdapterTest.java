package com.auta.server.adapter.out.redis;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class RedisAdapterTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisAdapter redisAdapter;

    @Test
    void store_shouldSaveTokenWithExpiration() {
        //given
        String key = "key: ";
        String token = "dummy-token";
        long expirationMillis = 60000L;

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        //when
        redisAdapter.store(key, token, expirationMillis);

        //then
        verify(valueOperations).set(eq(key), eq(token), eq(expirationMillis), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    void delete_shouldDeleteToken() {
        //given
        String key = "key: ";
        when(stringRedisTemplate.delete(key)).thenReturn(true);

        //when
        redisAdapter.delete(key);

        //then
        verify(stringRedisTemplate).delete(eq(key));
    }
}
