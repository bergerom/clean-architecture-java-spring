package details.repositories.mocking;

import core.entities.User;
import core.ports.driven.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UserStubRepository implements UserRepository {

    private final User user;

    public UserStubRepository(UUID userId) {
        this.user = new User(userId, "henry", 22);
    }

    @Override
    public void saveUser(User user) {
        // not implemented
    }

    @Override
    public List<User> getUsers(String nameContains) {
        // not implemented
        return null;
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return Optional.of(user);
    }
}
