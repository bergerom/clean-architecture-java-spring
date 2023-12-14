package details.repositories;

import core.entities.User;
import core.ports.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserFileRepository implements UserRepository {

    private final CoreFileDatabase coreFileDatabase;
    private final EntitySerializer<User> entitySerializer;

    public UserFileRepository(CoreFileDatabase coreFileDatabase, EntitySerializer<User> entitySerializer) {
        this.coreFileDatabase = coreFileDatabase;
        this.entitySerializer = entitySerializer;
    }

    @Override
    public void saveUser(User user) {
        coreFileDatabase.addNewLine(FileDatabaseTable.USER_TABLE, entitySerializer.toCsvLine(user));
    }

    @Override
    public List<User> getUsers(String nameContains) {
        return coreFileDatabase.readAllLines(FileDatabaseTable.USER_TABLE)
                .map(entitySerializer::parseLine)
                .filter(user -> user.name().contains(nameContains))
                .toList();
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return coreFileDatabase.readAllLines(FileDatabaseTable.USER_TABLE)
                .map(entitySerializer::parseLine)
                .filter(user -> user.userId().equals(userId))
                .findFirst();
    }

    // TODO : create a generic class for parse/toComaSeparated, put this class in the constructor of this class
    protected static User unserialize(String line) {
        String[] values = StringUtils.split(line, ",");
        return new User(UUID.fromString(values[0]), values[1], Integer.parseInt(values[2]));
    }



}
