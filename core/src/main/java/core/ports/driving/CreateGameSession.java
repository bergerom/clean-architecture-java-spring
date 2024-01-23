package core.ports.driving;

import java.io.IOException;
import java.util.UUID;

public interface CreateGameSession {
    UUID createGameSession(CreateGameSessionRequest createGameSessionRequest) throws IOException;

    record CreateGameSessionRequest(UUID userId) {
    }
}
