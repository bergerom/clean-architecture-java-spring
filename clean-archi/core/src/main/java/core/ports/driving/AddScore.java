package core.ports.driving;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

public interface AddScore {
    void addScoreForUser(AddScore.AddScoreRequest addScoreRequest) throws IOException;

    record AddScoreRequest(UUID userId, UUID gameSessionId, Integer score, Instant date) {
    }
}
