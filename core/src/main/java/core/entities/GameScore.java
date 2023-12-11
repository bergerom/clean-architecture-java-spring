package core.entities;

import java.util.UUID;

public record GameScore(UUID gameSessionId, UUID userId, Integer score) {}
