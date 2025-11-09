package dev.nitin.server.controller;

import dev.nitin.server.dto.post.PostResponseDto;
import dev.nitin.server.dto.user.UserLoginResponseDto;
import dev.nitin.server.dto.user.UserUpdateRequestDto;
import dev.nitin.server.security.CustomUserPrincipal;
import dev.nitin.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Optional<?>> getUser() {
        CustomUserPrincipal principal = (CustomUserPrincipal) org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String userId = principal.userId();
        Optional<?> userOptional = userService.getUser(userId);
        return ResponseEntity.ok(userOptional);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getUserPosts() {
        CustomUserPrincipal principal = (CustomUserPrincipal) org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String userId = principal.userId();
        return ResponseEntity.ok(userService.getPostsByUserId(userId));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        CustomUserPrincipal principal = (CustomUserPrincipal) org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String userId = principal.userId();
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PatchMapping
    public ResponseEntity<UserLoginResponseDto> updateUser(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        CustomUserPrincipal principal = (CustomUserPrincipal) org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String userId = principal.userId();
        UserLoginResponseDto updatedUser = userService.updateUser(userId, userUpdateRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

}
