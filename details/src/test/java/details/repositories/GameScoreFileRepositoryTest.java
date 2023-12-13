package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import core.ports.UserRepository;
import details.repositories.utils.FileDatabaseTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class GameScoreFileRepositoryTest {


    private FileDatabaseTestUtils fileDatabaseTestUtils = new FileDatabaseTestUtils(this.getClass());

    @Test
    public void testGetScoreForUser() throws IOException, URISyntaxException {
        UUID userId = UUID.randomUUID();
        UUID gameSessionId = UUID.randomUUID();
        List<GameScore> userScore = List.of(new GameScore(gameSessionId, userId, 5, Instant.now()),
                new GameScore(gameSessionId, userId, 10, Instant.now()), // test user has score of 5 + 10 = 15
                new GameScore(UUID.randomUUID(), UUID.randomUUID(), 11, Instant.now()));

        File userTable = fileDatabaseTestUtils.initializeDatabaseFile("score.csv", null); // TODO...
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(userTable.getParent());
        GameScoreFileRepository gameScoreFileRepository = new GameScoreFileRepository(coreFileDatabase);


        GameScore expectedGameScore = new GameScore(gameSessionId, userId, 15, Instant.now());

        Assertions.assertEquals(expectedGameScore, gameScoreFileRepository.getScoreForUser(userId));
    }

}