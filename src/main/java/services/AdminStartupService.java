package services;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Startup
@ApplicationScoped
public class AdminStartupService {

    private final UserService userService;

    @Inject
    public AdminStartupService(UserService userService) {
        this.userService = userService;
    }

    void onStart(@Observes StartupEvent event) {
        createAdmin();
    }

    @Transactional
    void createAdmin() {
        userService.createAdminIfNotExists();
    }
}
