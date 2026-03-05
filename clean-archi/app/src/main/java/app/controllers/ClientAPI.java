package app.controllers;

import app.containers.CreateUserContainer;
import app.controllers.DTO.CreateUserDTO;
import core.entities.User;
import core.export.dto.TotalScoreDTO;
import core.ports.driving.CreateUser;
import core.ports.driving.GlobalScoreBoard;
import core.ports.driving.ListUsers;
import core.usecases.UseCaseInteractor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class ClientAPI {

    private final UseCaseInteractor useCaseInteractor;

    public ClientAPI(UseCaseInteractor useCaseInteractor) {
        this.useCaseInteractor = useCaseInteractor;
    }

    @PostMapping("/users")
    public CreateUserDTO createUser(@RequestBody CreateUserContainer createUser) {
        CreateUser.CreateUserRequest createUserRequest
                = new CreateUser.CreateUserRequest(createUser.userName(), createUser.age());

        String userId =  useCaseInteractor.createUser(createUserRequest).id();

        return new CreateUserDTO(userId);
    }

    @GetMapping("/users")
    public List<User> listUsers(@RequestBody Optional<String> nameContains) {
        ListUsers.ListUserRequest listUserRequest
                = new ListUsers.ListUserRequest(nameContains.orElse(""));

        return useCaseInteractor.listUsers(listUserRequest).users();
    }

    @GetMapping("/scores")
    public List<TotalScoreDTO> getGlobalScoreBoard() {
        return useCaseInteractor.getGlobalScoreBoard(new GlobalScoreBoard.GlobalScoreBoardRequest()).totalScore();
    }
}
