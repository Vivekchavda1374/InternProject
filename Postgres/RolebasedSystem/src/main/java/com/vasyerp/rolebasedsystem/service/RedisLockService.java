package com.vasyerp.rolebasedsystem.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class RedisLockService {

    private static final DefaultRedisScript<Long> RELEASE_LOCK_SCRIPT = new DefaultRedisScript<>(
            """
                    if redis.call('get', KEYS[1]) == ARGV[1] then
                        return redis.call('del', KEYS[1])
                    else
                        return 0
                    end
                    """,
            Long.class);

    private final StringRedisTemplate redisTemplate;

    public RedisLockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryLock(String key, String ownerToken, Duration ttl) {
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, ownerToken, ttl);
        return Boolean.TRUE.equals(acquired);
    }

    public void unlock(String key, String ownerToken) {
        redisTemplate.execute(RELEASE_LOCK_SCRIPT, Collections.singletonList(key), ownerToken);
    }
}
