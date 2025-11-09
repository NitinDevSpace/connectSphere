package dev.nitin.server.dto.user;

import dev.nitin.server.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String password;

    public User toEntity() {
        return new User(name, email, password);
    }
}
