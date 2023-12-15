package core.ports.driven;

import core.entities.GameScore;
import core.entities.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GameScoreRepository {
    Map<UUID, List<GameScore>> getScoresForUser(List<User> users);
}
