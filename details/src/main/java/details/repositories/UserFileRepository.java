package details.repositories;

import core.entities.User;
import core.ports.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFileRepository implements UserRepository {

    private final CoreFileDatabase coreFileDatabase;

    public UserFileRepository(CoreFileDatabase coreFileDatabase) {
        this.coreFileDatabase = coreFileDatabase;
    }

    @Override
    public void saveUser(User user) {
        coreFileDatabase.addNewLine(FileDatabaseTable.USER_TABLE, toComaSeparated(user));
    }

    @Override
    public List<User> getUsers(String nameContains) {
        return coreFileDatabase.readAllLines(FileDatabaseTable.USER_TABLE)
                .map(UserFileRepository::parseToUser)
                .filter(user -> user.name().contains(nameContains))
                .toList();
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return coreFileDatabase.readAllLines(FileDatabaseTable.USER_TABLE)
                .map(UserFileRepository::parseToUser)
                .filter(user -> user.userId().equals(userId))
                .findFirst();
    }

    protected static User parseToUser(String line) {
        String[] values = StringUtils.split(line, ",");
        return new User(UUID.fromString(values[0]), values[1], Integer.parseInt(values[2]));
    }


    public static String toComaSeparated(User user) {
        return user.userId()
                + ","
                + user.name()
                + ","
                + user.age();
    }
}
