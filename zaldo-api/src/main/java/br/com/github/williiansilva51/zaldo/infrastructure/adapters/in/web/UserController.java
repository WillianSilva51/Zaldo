package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.application.service.user.CreateUserService;
import br.com.github.williiansilva51.zaldo.application.service.user.DeleteUserByIdService;
import br.com.github.williiansilva51.zaldo.application.service.user.FindUserByIdService;
import br.com.github.williiansilva51.zaldo.application.service.user.UpdateUserService;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.CreateUserRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.UpdateUserRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.UserResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final CreateUserService createUserService;
    private final FindUserByIdService findUserByIdService;
    private final UpdateUserService updateUserService;
    private final DeleteUserByIdService deleteUserByIdService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User domainObj = userMapper.toDomain(request);

        User created = createUserService.execute(domainObj);

        return ResponseEntity.created(URI.create("/users/" + created.getId())).body(userMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = findUserByIdService.execute(id);

        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserRequest request) {
        User newInfo = userMapper.toDomainByUpdate(request);

        User updated = updateUserService.execute(id, newInfo);

        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        deleteUserByIdService.execute(id);

        return ResponseEntity.noContent().build();
    }
}
