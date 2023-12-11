package details.repositories.mocking;

import core.entities.GameScore;
import core.entities.User;
import core.ports.GameScoreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// TODO : will be useful for later when we want to retrieve the score for a given user.. !
public class GameScoreFakeRepository implements GameScoreRepository {


    private final HashMap<UUID, List<GameScore>> userGameScores;

    @Override
    public GameScore getScoreForUser(UUID userId) {
        return null;
        //return userGameScores.get(userId).stream().mapToInt(gameScore -> gameScore.score()).sum();
    }

    public GameScoreFakeRepository(HashMap<UUID, List<GameScore>> userGameScores) {
        this.userGameScores = userGameScores;
    }
}
