package app.config.mock;

import app.security.DefaultUserToken;
import app.security.UserToken;
import core.ports.driven.GameScoreRepository;
import core.ports.driven.GameSessionRepository;
import core.ports.driven.UserRepository;
import core.usecases.UseCaseInteractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.UUID;

// TODO : see why the beans here are overrided by beans inside the app.config package..
@Configuration
public class MockBeans {
    @Bean
    public UseCaseInteractor getUseCaseInteractor(UserRepository userRepository,
                                                  GameScoreRepository gameScoreFileRepository,
                                                  GameSessionRepository gameSessionFileRepository) {
        return new UseCaseInteractor(userRepository, gameScoreFileRepository, gameSessionFileRepository);
    }

    @Bean
    @Primary
    public UserRepository getUserRepository() {
        return new UserStubRepository(UUID.randomUUID());
    }

    @Bean
    @Primary
    public GameScoreRepository getGameScoreRepository() {
        return null;
    }

    @Bean
    @Primary
    public UserToken getUserToken() {
        return new DefaultUserToken();
    }
}