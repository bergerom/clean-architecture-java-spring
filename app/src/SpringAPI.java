package com.example.cleanarchitecturejavaspring.app.src;

import com.example.cleanarchitecturejavaspring.app.src.containers.CreateUserContainer;
import com.example.cleanarchitecturejavaspring.core.src.main.java.ports.CreateUser;
import com.example.cleanarchitecturejavaspring.core.src.main.java.usecases.UseCaseInteractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class SpringAPI {

    private final UseCaseInteractor useCaseInteractor;

    public SpringAPI(UseCaseInteractor useCaseInteractor) {
        this.useCaseInteractor = useCaseInteractor;
    }

    @PostMapping
    public String createUser(@RequestBody CreateUserContainer createUser) {
        CreateUser.CreateUserRequest createUserRequest
                = new CreateUser.CreateUserRequest(createUser.userName(), createUser.age());
        return useCaseInteractor.createUser(createUserRequest).id();
    }
}
