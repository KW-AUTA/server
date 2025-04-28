package com.auta.server.adapter.out.redis;

import static org.assertj.core.api.Assertions.assertThat;

import com.auta.server.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

class RedisAdapterTest extends IntegrationTestSupport {
    @Autowired
    private RedisAdapter redisAdapter;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @DisplayName("키, 토큰, 캐시 만료기간을 받으면 레디스에 저장한다.")
    @Test
    void store() {
        //given
        String key = "key: ";
        String token = "dummy-token";
        long expirationMillis = 60000L;

        //when
        redisAdapter.store(key, token, expirationMillis);

        //then
        String storedToken = redisTemplate.opsForValue().get(key);
        assertThat(storedToken).isEqualTo(token);
    }
}