package dev.nitin.server.controller;

import dev.nitin.server.dto.user.UserLoginRequestDto;
import dev.nitin.server.dto.user.UserLoginResponseDto;
import dev.nitin.server.dto.user.UserRegisterRequestDto;
import dev.nitin.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        userService.registerUser(userRegisterRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserLoginResponseDto responseDto = userService.loginUser(userLoginRequestDto);
        return ResponseEntity.ok(responseDto);
    }
}
