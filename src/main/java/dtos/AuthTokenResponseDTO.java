package dtos;

public record AuthTokenResponseDTO(String token, long expiresIn) {
}
