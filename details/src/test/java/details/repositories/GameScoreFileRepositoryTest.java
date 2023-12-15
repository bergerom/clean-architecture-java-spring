package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import details.repositories.utils.FileDatabaseTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class GameScoreFileRepositoryTest {


    private FileDatabaseTestUtils fileDatabaseTestUtils = new FileDatabaseTestUtils(this.getClass());

    @Test
    public void testGetGlobalScoreboard() throws IOException, URISyntaxException {
        UUID gameScoreId = UUID.randomUUID();
        User userOne = new User(UUID.randomUUID(), "henry", 24);
        User userTwo = new User(UUID.randomUUID(), "sarah", 26);
        UUID gameSessionId = UUID.randomUUID();
        List<Record> gameScores = List.of(new GameScore(gameScoreId, gameSessionId, userOne.userId(), 5, Instant.now()),
                new GameScore(gameScoreId, gameSessionId, userOne.userId(), 10, Instant.now()), // test user has score of 5 + 10 = 15
                new GameScore(gameScoreId, UUID.randomUUID(), userTwo.userId(), 11, Instant.now()));

        File userTable = fileDatabaseTestUtils.initializeDatabaseFile("score.csv", gameScores);
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(userTable.getParent());
        GameScoreFileRepository gameScoreFileRepository = new GameScoreFileRepository(coreFileDatabase);

        List<User> users = List.of(userOne, userTwo);

        Map<UUID, List<GameScore>> globalScoreBoard = gameScoreFileRepository.getScoresForUser(users);
        Integer numberOfScoreUserOne = globalScoreBoard.get(userOne.userId()).size();
        Integer numberOfScoreUserTwo = globalScoreBoard.get(userTwo.userId()).size();

        Assertions.assertEquals(2, numberOfScoreUserOne);
        Assertions.assertEquals(1, numberOfScoreUserTwo);
    }

}