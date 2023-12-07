package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ports.UserRepository;
import usecases.UseCaseInteractor;
import repositories.FileDatabase;

@Configuration
public class CoreBeans {
    @Bean
    public UseCaseInteractor getUseCaseInteractor() {
        UserRepository userRepository = new FileDatabase("");
        return new UseCaseInteractor(userRepository);
    }
}
