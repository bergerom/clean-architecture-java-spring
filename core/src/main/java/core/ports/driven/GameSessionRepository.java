package core.ports.driven;

import core.entities.GameSession;

public interface GameSessionRepository {

    void addGameSession(GameSession session);
}
