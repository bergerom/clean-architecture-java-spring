package app.config.mock;

import app.security.DefaultUserToken;
import app.security.UserToken;
import core.ports.driven.GameScoreRepository;
import core.ports.driven.GameSessionRepository;
import core.ports.driven.UserRepository;
import core.usecases.UseCaseInteractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.UUID;

@Configuration
public class MockBeans {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public UseCaseInteractor getUseCaseInteractor(UserRepository userRepository,
                                                  GameScoreRepository gameScoreFileRepository,
                                                  GameSessionRepository gameSessionFileRepository) {
        return new UseCaseInteractor(userRepository, gameScoreFileRepository, gameSessionFileRepository);
    }

    @Bean
    public UserRepository getUserRepository() {
        return new UserStubRepository(UUID.randomUUID());
    }

    @Bean
    public GameScoreRepository getGameScoreRepository() {
        return null;
    }

    @Bean
    public UserToken getUserToken() {
        return new DefaultUserToken();
    }
}