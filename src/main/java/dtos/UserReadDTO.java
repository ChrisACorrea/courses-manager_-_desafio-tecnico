package dtos;

import entities.User;

public record UserReadDTO(Long id, String name, String email, String role) {

    public static UserReadDTO fromEntity(User user) {
        return new UserReadDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
