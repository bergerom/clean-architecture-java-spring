package details.repositories.builder;
import core.entities.User;

import java.util.UUID;

public class UserBuilder {
    private User user;

    public UserBuilder() {}

    public UserBuilder fromArray(String[] values) {
        user = new User(UUID.fromString(values[0]), values[1], Integer.parseInt(values[2]));
        return this;
    }

    public User build() {
        return user;
    }
}
