package com.example.cleanarchitecturejavaspring.app.src.config;

import com.example.cleanarchitecturejavaspring.core.src.main.java.usecases.UseCaseInteractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreBeans {
    @Bean
    public UseCaseInteractor getUseCaseInteractor() {
        return new UseCaseInteractor();
    }
}
