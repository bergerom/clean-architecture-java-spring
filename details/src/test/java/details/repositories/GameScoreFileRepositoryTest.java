package details.repositories;

import core.entities.GameScore;
import details.repositories.utils.FileDatabaseTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

class GameScoreFileRepositoryTest {


    private FileDatabaseTestUtils fileDatabaseTestUtils = new FileDatabaseTestUtils(this.getClass());

    @Test
    public void testTotalScoreForUser() throws IOException, URISyntaxException {
        UUID gameScoreId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID gameSessionId = UUID.randomUUID();
        List<Record> gameScores = List.of(new GameScore(gameScoreId, gameSessionId, userId, 5, Instant.now()),
                new GameScore(gameScoreId, gameSessionId, userId, 10, Instant.now()), // test user has score of 5 + 10 = 15
                new GameScore(gameScoreId, UUID.randomUUID(), UUID.randomUUID(), 11, Instant.now()));

        File userTable = fileDatabaseTestUtils.initializeDatabaseFile("score.csv", gameScores);
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(userTable.getParent());
        GameScoreFileRepository gameScoreFileRepository = new GameScoreFileRepository(coreFileDatabase);


        Assertions.assertEquals(15, gameScoreFileRepository.getTotalScoreForUser(userId));
    }

}