package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class UserSessionManager {
    private static final Duration EXPIRATION = Duration.ofMinutes(30);
    private final RedisTemplate<String, FlowContext> redisTemplate;

    public UserSessionManager(@Qualifier("flowContextRedisTemplate") RedisTemplate<String, FlowContext> redis) {
        redisTemplate = redis;
    }

    private @NonNull String getKey(Long chatId) {
        return "session:" + chatId;
    }

    public void save(Long userId, FlowContext flowContext) {
        String key = getKey(userId);
        redisTemplate.opsForValue()
                .set(key, flowContext, EXPIRATION);
    }


    public FlowContext get(Long chatId) {
        String key = getKey(chatId);

        FlowContext flowContext = redisTemplate.opsForValue().get(key);

        if (flowContext == null) {
            return new FlowContext();
        }

        return flowContext;
    }

    public void clearSession(Long chatId) {
        String key = getKey(chatId);
        redisTemplate.delete(key);
    }

    public void setChatState(Long chatId, ChatState chatState) {
        FlowContext flowContext = get(chatId);

        flowContext.setChatState(chatState);

        save(chatId, flowContext);
    }
}
