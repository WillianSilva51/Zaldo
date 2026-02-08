package br.com.github.williiansilva51.zaldo.infrastructure.config;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean(name = "flowContextRedisTemplate")
    public RedisTemplate<String, FlowContext> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, FlowContext> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        JacksonJsonRedisSerializer<FlowContext> jsonSerializer = new JacksonJsonRedisSerializer<>(FlowContext.class);
        redisTemplate.setValueSerializer(jsonSerializer);

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jsonSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
