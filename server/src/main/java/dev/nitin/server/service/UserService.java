package dev.nitin.server.service;

import dev.nitin.server.dto.post.PostResponseDto;
import dev.nitin.server.dto.user.UserLoginRequestDto;
import dev.nitin.server.dto.user.UserLoginResponseDto;
import dev.nitin.server.dto.user.UserRegisterRequestDto;
import dev.nitin.server.dto.user.UserUpdateRequestDto;
import dev.nitin.server.exception.ResourceNotFoundException;
import dev.nitin.server.model.User;
import dev.nitin.server.repository.PostRepository;
import dev.nitin.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final UserRepository userRepository;
    private   final PostRepository postRepository;

    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        if (userRepository.existsByEmail(userRegisterRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = userRegisterRequestDto.toEntity();
        userRepository.save(user);
    }

    public UserLoginResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> userOptional = userRepository.findByEmailAndPassword(
                userLoginRequestDto.getEmail(),
                userLoginRequestDto.getPassword()
        );

        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        User user = userOptional.get();
        return UserLoginResponseDto.toDto(user);
    }

    public UserLoginResponseDto updateUser(String userId, UserUpdateRequestDto updatedUserDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updatedUserDto.getName() != null) existingUser.setName(updatedUserDto.getName());
        if (updatedUserDto.getEmail() != null) existingUser.setEmail(updatedUserDto.getEmail());
        if (updatedUserDto.getPassword() != null) existingUser.setPassword(updatedUserDto.getPassword());

        userRepository.save(existingUser);

         return UserLoginResponseDto.toDto(existingUser);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }


    public List<PostResponseDto> getPostsByUserId(String userId) {
        // Check if the user exists first
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return postRepository.findAllByUserId(userId)
                .stream()
                .map(PostResponseDto::toDto)
                .toList();
    }


}
