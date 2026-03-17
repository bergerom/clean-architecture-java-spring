package app.config.mock;

import core.entities.User;
import core.ports.driven.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UserStubRepository implements UserRepository {

    private final User user;

    private final List<User> users = new ArrayList<>();

    public UserStubRepository(UUID userId) {
        this.user = new User(userId, "henry", 22);
        users.add(user);
    }

    @Override
    public void saveUser(User user) {
        users.add(user);
    }

    @Override
    public List<User> getUsers(String nameContains) {
        System.out.println("Calling users in stub");
        return users;
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return Optional.of(user);
    }
}
