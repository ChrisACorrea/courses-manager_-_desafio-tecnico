package repositories;

import java.util.Optional;

import entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, Long> {

    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
