package app.config;

import core.ports.GameScoreRepository;
import details.repositories.CoreFileDatabase;
import details.repositories.GameScoreFileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import core.ports.UserRepository;
import core.usecases.UseCaseInteractor;
import details.repositories.UserFileRepository;

@Configuration
public class CoreBeans {
    @Bean
    public UseCaseInteractor getUseCaseInteractor() {

        // File database access layer
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase("");

        // Repositories
        UserRepository userRepository = new UserFileRepository(coreFileDatabase);
        GameScoreRepository gameScoreFileRepository = new GameScoreFileRepository("");

        return new UseCaseInteractor(userRepository, gameScoreFileRepository);
    }
}