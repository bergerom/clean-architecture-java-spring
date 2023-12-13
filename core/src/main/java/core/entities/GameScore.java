package core.entities;

import java.time.Instant;
import java.util.UUID;

public record GameScore(UUID gameSessionId, UUID userId, Integer score, Instant date) {}
