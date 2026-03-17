package app.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @ConditionalOnProperty(name = "app.security.oauth2.enabled", havingValue = "true")
    public SecurityFilterChain securityWithOAuth2(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // or cookie-based CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/private/**").hasRole("")
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "app.security.oauth2.enabled", havingValue = "false", matchIfMissing = true)
    public SecurityFilterChain securityNoOAuth2(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // or cookie-based CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}

