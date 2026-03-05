package core.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record GameScore(UUID gameScoreId, UUID gameSessionId, UUID userId, Integer score, Instant date) implements Serializable {}
