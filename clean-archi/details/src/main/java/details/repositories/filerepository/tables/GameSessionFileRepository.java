package details.repositories.filerepository.tables;

import core.entities.GameSession;
import core.ports.driven.GameSessionRepository;
import details.repositories.filerepository.implementation.CoreFileDatabase;
import details.repositories.filerepository.implementation.EntitySerializer;


public class GameSessionFileRepository implements GameSessionRepository {

    private final CoreFileDatabase coreFileDatabase;
    private final EntitySerializer<GameSession> entitySerializer;


    public GameSessionFileRepository(CoreFileDatabase coreFileDatabase, EntitySerializer<GameSession> entitySerializer) {
        this.coreFileDatabase = coreFileDatabase;
        this.entitySerializer = entitySerializer;
    }

    @Override
    public void addGameSession(GameSession session) {
        String csvLine = entitySerializer.toCsvLine(session);
        coreFileDatabase.addNewLine(FileDatabaseTable.SESSION_TABLE, csvLine);
    }

}
