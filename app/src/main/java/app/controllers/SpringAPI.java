package app.controllers;

import app.containers.CreateUserContainer;
import core.entities.User;
import core.ports.CreateUser;
import core.ports.ListUsers;
import core.usecases.UseCaseInteractor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public List<User> listUsers(@RequestBody Optional<String> nameContains) {
        ListUsers.ListUserRequest listUserRequest
                = new ListUsers.ListUserRequest(nameContains.orElse(""));

        return useCaseInteractor.listUsers(listUserRequest).users();
    }
}
