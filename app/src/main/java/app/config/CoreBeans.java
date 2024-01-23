package app.config;

import app.security.DefaultUserToken;
import app.security.UserToken;
import core.entities.GameScore;
import core.entities.GameSession;
import core.entities.User;
import core.ports.driven.GameScoreRepository;
import core.ports.driven.GameSessionRepository;
import core.ports.driven.UserRepository;
import core.usecases.UseCaseInteractor;
import details.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreBeans {
   @Bean
    public UseCaseInteractor getUseCaseInteractor(UserRepository userRepository,
                                                  GameScoreRepository gameScoreFileRepository,
                                                  GameSessionRepository gameSessionFileRepository) {

        // File database access layer
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase("");


        return new UseCaseInteractor(userRepository, gameScoreFileRepository, gameSessionFileRepository);
    }

    @Bean
    public UserRepository getUserRepository() {
        // File database access layer
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase("");

        // User Driven port implementation
        EntitySerializer<User> userEntitySerializer = new EntitySerializer<>(User.class);

        return new UserFileRepository(coreFileDatabase, userEntitySerializer);
    }

    @Bean
    public GameScoreRepository getGameScoreRepository() {
        // File database access layer
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase("");

        // Game Score Driven port implementation
        EntitySerializer<GameScore> gameScoreEntitySerializer = new EntitySerializer<>(GameScore.class);
        return new GameScoreFileRepository(coreFileDatabase, gameScoreEntitySerializer);
    }

    @Bean
    public GameSessionRepository getGameSessionRepository() {
        // File database access layer
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase("");

        // Game Session Driven port implementation
        EntitySerializer<GameSession> gameSessionEntitySerializer = new EntitySerializer<>(GameSession.class);
        return new GameSessionFileRepository(coreFileDatabase, gameSessionEntitySerializer);
    }

    @Bean
    public UserToken getUserToken() {
        return new DefaultUserToken();
    }
}
