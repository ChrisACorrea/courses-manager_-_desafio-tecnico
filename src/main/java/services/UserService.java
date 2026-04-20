package services;

import java.time.Instant;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dtos.AuthTokenRequestDTO;
import dtos.AuthTokenResponseDTO;
import dtos.UserCreateDTO;
import entities.User;
import exceptions.ConflictException;
import exceptions.ResourceNotFoundException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import repositories.UserRepository;
import results.Result;

@ApplicationScoped
public class UserService {

    private static final String DEFAULT_ADMIN_EMAIL = "admin";

    private final UserRepository userRepository;
    private final Validator validator;

    @ConfigProperty(name = "auth.jwt.expiration", defaultValue = "3600")
    long jwtExpiration;

    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "courses-manager")
    String jwtIssuer;

    @Inject
    public UserService(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public Result<User> createUser(UserCreateDTO userCreateDTO) {
        var dtoViolations = validator.validate(userCreateDTO);

        if (!dtoViolations.isEmpty()) {
            var errors = dtoViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        if (userRepository.findByEmail(userCreateDTO.email()).isPresent()) {
            throw new ConflictException("E-mail já cadastrado.");
        }

        var encodedPassword = BcryptUtil.bcryptHash(userCreateDTO.password());
        var user = userCreateDTO.toEntity(encodedPassword, User.ROLE_USER);

        var entityViolations = validator.validate(user);
        if (!entityViolations.isEmpty()) {
            var errors = entityViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        userRepository.persist(user);
        return Result.success(user);
    }

    public Result<AuthTokenResponseDTO> authenticate(AuthTokenRequestDTO authTokenRequestDTO) {
        var dtoViolations = validator.validate(authTokenRequestDTO);
        if (!dtoViolations.isEmpty()) {
            var errors = dtoViolations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();
            return Result.failure(errors);
        }

        var userOpt = userRepository.findByEmail(authTokenRequestDTO.email());
        if (userOpt.isEmpty() || !BcryptUtil.matches(authTokenRequestDTO.password(), userOpt.get().getPassword())) {
            return Result.failure(java.util.List.of("Credenciais inválidas."));
        }

        var user = userOpt.get();
        var expiresAt = Instant.now().plusSeconds(jwtExpiration);

        var token = Jwt.issuer(jwtIssuer)
                .subject(user.getEmail())
                .upn(user.getEmail())
                .groups(Set.of(user.getRole()))
                .expiresAt(expiresAt)
                .sign();

        return Result.success(new AuthTokenResponseDTO(token, jwtExpiration));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    public void createAdminIfNotExists() {
        if (userRepository.findByEmail(DEFAULT_ADMIN_EMAIL).isPresent()) {
            return;
        }

        var encodedPassword = BcryptUtil.bcryptHash("admin");
        var user = new User("admin", DEFAULT_ADMIN_EMAIL, encodedPassword, User.ROLE_ADMIN);
        userRepository.persist(user);
    }
}
