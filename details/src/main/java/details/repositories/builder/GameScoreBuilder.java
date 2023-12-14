package details.repositories.builder;

import core.entities.GameScore;

import java.time.Instant;
import java.util.UUID;

public class GameScoreBuilder {
    public GameScore gameScore;

    public GameScoreBuilder() {
    }

    public GameScoreBuilder fromArray(String[] values) {
        gameScore = new GameScore(UUID.fromString(values[0]),
                UUID.fromString(values[1]),
                UUID.fromString(values[2]),
                Integer.parseInt(values[3]),
                Instant.parse(values[4]));
        return this;
    }

    public GameScore build() {
        return gameScore;
    }
}
