package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import core.ports.GameScoreRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class GameScoreFileRepository implements GameScoreRepository {

    private final CoreFileDatabase coreFileDatabase;


    public GameScoreFileRepository(CoreFileDatabase coreFileDatabase) {
        this.coreFileDatabase = coreFileDatabase;
    }

    @Override
    public GameScore getScoreForUser(UUID userId) {
        return null;
    }

    protected User parseToGameScore(String line) {
        // TODO : implement
        String[] values = StringUtils.split(line, ",");
        return new User(UUID.fromString(values[0]), values[1], Integer.parseInt(values[2]));
    }


    protected String toComaSeparated(User user) {
        // TODO : implement
        return user.userId()
                + ","
                + user.name()
                + ","
                + user.age();
    }
}
