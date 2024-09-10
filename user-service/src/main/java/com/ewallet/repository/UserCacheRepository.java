package com.ewallet.repository;

import com.ewallet.model.User;
import com.ewallet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class UserCacheRepository {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Value("${user.redis.key.expiry:86400}")
    long expiry;

    public void save(User user) {
        redisTemplate.opsForValue().set(getKey(user.getUserId()), user, expiry, TimeUnit.SECONDS);
    }

    public User get(int userId) {
        return (User) redisTemplate.opsForValue().get(getKey(userId));
    }

    private String getKey(int userId) {
        return Constants.USER_REDIS_KEY_PREFIX + userId;
    }

}
