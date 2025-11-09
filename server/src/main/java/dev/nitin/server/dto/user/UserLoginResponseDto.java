package dev.nitin.server.dto.user;

import dev.nitin.server.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private String userId;
    private String name;
    private String email;

    public static UserLoginResponseDto toDto(User user) {
        return new UserLoginResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
