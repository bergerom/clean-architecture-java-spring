package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import core.ports.driven.GameScoreRepository;
import details.repositories.builder.GameScoreBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GameScoreFileRepository implements GameScoreRepository {

    private final CoreFileDatabase coreFileDatabase;
    private final EntitySerializer<GameScore> entitySerializer;


    public GameScoreFileRepository(CoreFileDatabase coreFileDatabase, EntitySerializer<GameScore> entitySerializer) {
        this.coreFileDatabase = coreFileDatabase;
        this.entitySerializer = entitySerializer;
    }

    @Override
    public Map<UUID, List<GameScore>> getScoresForUser(List<User> users) {

        return readFileAndParse()
                // Group by user
                .collect(Collectors.groupingBy(GameScore::userId));
    }

    @Override
    public void addScore(GameScore score) {
        // Handle case where score already exists
        if (readFileAndParse().anyMatch(sc -> sc.date().equals(score.date()))) {
            return;
        }
        String csvLine = entitySerializer.toCsvLine(score);
        coreFileDatabase.addNewLine(FileDatabaseTable.SCORE_TABLE, csvLine);
    }

    private Stream<GameScore> readFileAndParse() {
        return coreFileDatabase.readAllLines(FileDatabaseTable.SCORE_TABLE)
                .map(line -> line.split(","))
                .map(values -> new GameScoreBuilder().fromArray(values).build());
    }
}
