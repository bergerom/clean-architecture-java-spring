package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import core.ports.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class GameScoreFileRepositoryTest {
    

    
    @Test
    public void testGetScoreForUser() {
        GameScoreFileRepository userFileRepository = new GameScoreFileRepository("");

        UUID userId = UUID.randomUUID();
        UUID gameSessionId = UUID.randomUUID();
        GameScore expectedGameScore = new GameScore(gameSessionId, userId, 15);

        Assertions.assertEquals(expectedGameScore, userFileRepository.getScoreForUser(userId));
    }

}