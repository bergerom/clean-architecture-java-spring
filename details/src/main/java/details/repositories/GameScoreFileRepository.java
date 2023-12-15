package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import core.ports.driven.GameScoreRepository;
import details.repositories.builder.GameScoreBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class GameScoreFileRepository implements GameScoreRepository {

    private final CoreFileDatabase coreFileDatabase;


    public GameScoreFileRepository(CoreFileDatabase coreFileDatabase) {
        this.coreFileDatabase = coreFileDatabase;
    }

    @Override
    public Map<UUID, List<GameScore>> getScoresForUser(List<User> users) {

        return coreFileDatabase.readAllLines(FileDatabaseTable.SCORE_TABLE)
                .map(line -> line.split(","))
                .map(values -> new GameScoreBuilder().fromArray(values).build())
                // Group by user
                .collect(Collectors.groupingBy(GameScore::userId));


    }
}
