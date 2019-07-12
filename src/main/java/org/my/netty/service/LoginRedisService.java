package org.my.netty.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class LoginRedisService {

    @Resource(name = "loginRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.login.prefix}")
    String tokenPre;

    // ============================String=============================

    /**
     * 获取登录用户的token
     *
     * @param key 键
     * @return 值
     */
    public Object getToken(int uid) {
        String key = tokenPre + uid;
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
}