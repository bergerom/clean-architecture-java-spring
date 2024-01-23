package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Implementations of these methods should launch the framework on the port
 * defined inside the test/java/karate-config.js
 * <p>
 * For now, only spring boot is implemented (as default here).
 */
@SpringBootApplication
public interface FrameworkLauncher {

    default void launch() {
        SpringApplication.run(app.CleanArchitectureJavaSpringApplication.class);
    }
}