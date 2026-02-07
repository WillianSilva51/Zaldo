package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class UserSessionManager {
    private static final Duration EXPIRATION = Duration.ofMinutes(30);
    private final RedisTemplate<String, FlowContext> redisTemplate;

    private @NonNull String getKey(Long userId) {
        return "session:" + userId;
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
}
