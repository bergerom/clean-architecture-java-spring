package app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class BeanCheckConfig {

    @Bean
    public CommandLineRunner checkClientRegistrationRepo(ApplicationContext ctx) {
        return args -> {
            boolean present = !ctx.getBeansOfType(ClientRegistrationRepository.class).isEmpty();
            System.out.println("ClientRegistrationRepository present: " + present);
            ctx.getBeansOfType(ClientRegistrationRepository.class)
                .forEach((name, bean) -> System.out.println(" - bean: " + name + " -> " + bean.getClass().getName()));
        };
    }
}

