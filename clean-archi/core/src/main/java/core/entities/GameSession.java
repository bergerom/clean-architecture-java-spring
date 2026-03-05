package core.entities;

import java.io.Serializable;
import java.util.UUID;

public record GameSession(UUID gameSessionId, UUID userId) implements Serializable {};
