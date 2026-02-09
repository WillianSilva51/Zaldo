package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.application.service.user.*;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.CreateUserRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.UpdateUserRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.UserResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints to manage users")
public class UserController {
    private final CreateUserService createUserService;
    private final ListUsersService listUsersService;
    private final FindUserByIdService findUserByIdService;
    private final UpdateUserService updateUserService;
    private final DeleteUserByIdService deleteUserByIdService;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Creates a new user", description = "Returns the created user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User domainObj = userMapper.toDomain(request);

        User created = createUserService.execute(domainObj);

        return ResponseEntity.created(URI.create("/users/" + created.getId())).body(userMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Lists all users", description = "Returns a list of users")
    @ApiResponse(responseCode = "200", description = "Users found successfully")
    public ResponseEntity<List<UserResponse>> listAllUsers(@RequestParam(required = false) String emailFragment) {
        List<UserResponse> responseList = listUsersService.execute(emailFragment)
                .stream()
                .map(userMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds a user by id", description = "Returns the user found")
    @ApiResponse(responseCode = "200", description = "User found successfully")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = findUserByIdService.execute(id);

        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a user by id", description = "Returns the updated user")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserRequest request) {
        User newInfo = userMapper.toDomainByUpdate(request);

        User updated = updateUserService.execute(id, newInfo);

        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a user by id", description = "Returns no content")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        deleteUserByIdService.execute(id);

        return ResponseEntity.noContent().build();
    }
}
