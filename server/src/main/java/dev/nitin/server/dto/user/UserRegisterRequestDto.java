package dev.nitin.server.dto.user;

import dev.nitin.server.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public User toEntity() {
        return new User(name, email, password);
    }
}
