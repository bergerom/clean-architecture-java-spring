package core.ports.driven;


import core.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void saveUser(User user);

    List<User> getUsers(String nameContains);

    Optional<User> getUser(UUID userId);
}
