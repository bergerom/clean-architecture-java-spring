package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import core.ports.GameScoreRepository;
import details.repositories.builder.GameScoreBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class GameScoreFileRepository implements GameScoreRepository {

    private final CoreFileDatabase coreFileDatabase;


    public GameScoreFileRepository(CoreFileDatabase coreFileDatabase) {
        this.coreFileDatabase = coreFileDatabase;
    }

    @Override
    public Integer getTotalScoreForUser(UUID userId) {
        // User is the aggragate root, can be retrieved here
         return coreFileDatabase.readAllLines(FileDatabaseTable.SCORE_TABLE)
                 .map(line -> line.split(","))
                 .map(values -> new GameScoreBuilder().fromArray(values).build())
                 .filter(score -> score.userId().equals(userId))
                 .mapToInt(GameScore::score)
                 .sum();
    }
}
