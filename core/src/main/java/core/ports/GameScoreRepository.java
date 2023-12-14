package core.ports;

import java.util.UUID;

public interface GameScoreRepository {
    Integer getTotalScoreForUser(UUID userId);
}
