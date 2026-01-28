package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.CreateUserRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.UpdateUserRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, CreateUserRequest, UserResponse, UpdateUserRequest> {
    @Override
    public User toDomain(CreateUserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    @Override
    public User toDomainByUpdate(UpdateUserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    @Override
    public UserResponse toResponse(User domain) {
        return new UserResponse(
                domain.getId(),
                domain.getName(),
                domain.getEmail());
    }
}
