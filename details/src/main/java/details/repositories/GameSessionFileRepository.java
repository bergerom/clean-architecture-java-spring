package details.repositories;

import core.entities.GameScore;
import core.entities.GameSession;
import core.entities.User;
import core.ports.driven.GameScoreRepository;
import core.ports.driven.GameSessionRepository;
import details.repositories.builder.GameScoreBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GameSessionFileRepository implements GameSessionRepository {

    private final CoreFileDatabase coreFileDatabase;
    private final EntitySerializer<GameSession> entitySerializer;


    public GameSessionFileRepository(CoreFileDatabase coreFileDatabase, EntitySerializer<GameSession> entitySerializer) {
        this.coreFileDatabase = coreFileDatabase;
        this.entitySerializer = entitySerializer;
    }

    @Override
    public void addGameSession(GameSession session) {
        String csvLine = entitySerializer.toCsvLine(session);
        coreFileDatabase.addNewLine(FileDatabaseTable.SESSION_TABLE, csvLine);
    }

}
