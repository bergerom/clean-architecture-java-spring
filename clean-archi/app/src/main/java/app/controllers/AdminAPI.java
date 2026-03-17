package app.controllers;

import app.containers.CreateUserContainer;
import app.controllers.DTO.CreateUserDTO;
import core.ports.driving.CreateUser;
import core.usecases.UseCaseInteractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AdminAPI {
    private final UseCaseInteractor useCaseInteractor;

    public AdminAPI(UseCaseInteractor useCaseInteractor) {
        this.useCaseInteractor = useCaseInteractor;
    }

    @PostMapping("/admin/users")
    public CreateUserDTO createUser(@RequestBody CreateUserContainer createUser) {
        CreateUser.CreateUserRequest createUserRequest
                = new CreateUser.CreateUserRequest(createUser.userName(), createUser.age());

        String userId =  useCaseInteractor.createUser(createUserRequest).id();

        return new CreateUserDTO(userId);
    }
}
