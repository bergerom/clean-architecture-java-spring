package app.config;

import app.security.DefaultUserToken;
import app.security.UserToken;
import core.entities.GameScore;
import core.entities.User;
import core.ports.driven.GameScoreRepository;
import core.ports.driven.UserRepository;
import core.usecases.UseCaseInteractor;
import details.repositories.CoreFileDatabase;
import details.repositories.EntitySerializer;
import details.repositories.GameScoreFileRepository;
import details.repositories.UserFileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreBeans {
    @Bean
    public UseCaseInteractor getUseCaseInteractor() {

        // File database access layer
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase("");

        // Game Score Driven port implementation
        EntitySerializer<GameScore> gameScoreEntitySerializer = new EntitySerializer<>(GameScore.class);
        GameScoreRepository gameScoreFileRepository =
                new GameScoreFileRepository(coreFileDatabase, gameScoreEntitySerializer);

        // User Driven port implementation
        EntitySerializer<User> userEntitySerializer = new EntitySerializer<>(User.class);
        UserRepository userRepository = new UserFileRepository(coreFileDatabase, userEntitySerializer);

        return new UseCaseInteractor(userRepository, gameScoreFileRepository);
    }

    @Bean
    public UserToken getUserToken() {
        return new DefaultUserToken();
    }
}
