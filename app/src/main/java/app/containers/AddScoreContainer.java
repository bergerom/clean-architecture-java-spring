package app.containers;

import java.time.Instant;
import java.util.UUID;

public record AddScoreContainer(UUID gameSessionId, Integer score, Instant date) {
}
