package org.my.netty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 配置自定义redisTemplate. 方法名一定要叫redisTemplate 因为@Bean注解是根据方法名配置这个bean的name
     * 
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> myRedisTemplate(
            @Value("${redis.my.host}") String host,
            @Value("${redis.my.port}") int port,
            @Value("${redis.my.password}") String password,
            @Value("${redis.my.database}") int database) {
        
        RedisStandaloneConfiguration config=new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setDatabase(database);
        config.setPassword(RedisPassword.of(password));
        config.setPort(port);
        
        LettuceConnectionFactory factory=new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setValueSerializer(new StringRedisSerializer());
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String,Object> loginRedisTemplate(
            @Value("${redis.login.host}") String host,
            @Value("${redis.login.port}") int port,
            @Value("${redis.login.password}") String password,
            @Value("${redis.login.database}") int database){
        RedisStandaloneConfiguration config=new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setDatabase(database);
        config.setPassword(RedisPassword.of(password));
        config.setPort(port);
        
        LettuceConnectionFactory factory=new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();
        
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setValueSerializer(new StringRedisSerializer());
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
