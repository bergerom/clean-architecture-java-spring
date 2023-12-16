package app.controllers;

import app.containers.AddScoreContainer;
import app.containers.CreateUserContainer;
import app.security.UserToken;
import core.entities.User;
import core.export.dto.TotalScoreDTO;
import core.ports.driving.AddScore;
import core.ports.driving.CreateUser;
import core.ports.driving.GlobalScoreBoard;
import core.ports.driving.ListUsers;
import core.usecases.UseCaseInteractor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class SecureAPI {

    private final UseCaseInteractor useCaseInteractor;
    private final UserToken userToken;

    public SecureAPI(UseCaseInteractor useCaseInteractor, UserToken userToken) {
        this.useCaseInteractor = useCaseInteractor;
        this.userToken = userToken;
    }

    @PostMapping("/secure/scores")
    public void addScoreForUser(@RequestBody AddScoreContainer score) {
        UUID userId = userToken.getUserId();
        AddScore.AddScoreRequest addScoreRequest = new AddScore.AddScoreRequest(
                userId,
                score.gameSessionId(),
                score.score(),
                score.date()
        );

        useCaseInteractor.addScoreForUser(addScoreRequest);
    }
}
