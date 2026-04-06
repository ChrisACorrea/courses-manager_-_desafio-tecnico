package dtos;

import entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password) {

    public User toEntity(String encodedPassword, String role) {
        return new User(name, email, encodedPassword, role);
    }
}
