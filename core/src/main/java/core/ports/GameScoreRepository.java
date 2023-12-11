package core.ports;

import core.entities.GameScore;

import java.util.UUID;

public interface GameScoreRepository {
    GameScore getScoreForUser(UUID userId);
}
