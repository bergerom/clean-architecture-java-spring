package details.repositories;

import core.entities.GameSession;
import details.repositories.utils.FileDatabaseTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class GameSessionFileRepositoryTest {

    private final FileDatabaseTestUtils fileDatabaseTestUtils = new FileDatabaseTestUtils(this.getClass());

    @Test
    public void testAddSession() throws Exception {

        String csvTableName = FileDatabaseTable.SESSION_TABLE.toString();

        File sessionTable = fileDatabaseTestUtils.initializeDatabaseFile(csvTableName, Collections.emptyList());
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(sessionTable.getParent());
        EntitySerializer<GameSession> gameSessionEntitySerializer = new EntitySerializer<>(GameSession.class);
        GameSessionFileRepository gameSessionFileRepository
                = new GameSessionFileRepository(coreFileDatabase, gameSessionEntitySerializer);

        GameSession gameSession = getGameSession();
        gameSessionFileRepository.addGameSession(gameSession);
        gameSessionFileRepository.addGameSession(gameSession); // duplicate values not allowed

        List<String> fileLines = coreFileDatabase.readAllLines(FileDatabaseTable.SESSION_TABLE).toList();
        Assertions.assertEquals(2, fileLines.size());
    }

    private GameSession getGameSession() {
        return new GameSession(UUID.randomUUID(), UUID.randomUUID());
    }

}