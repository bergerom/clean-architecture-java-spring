package app.controllers;

import app.containers.AddScoreContainer;
import app.security.UserToken;
import core.ports.driving.AddScore;
import core.usecases.UseCaseInteractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/secure/score")
    @ResponseStatus(HttpStatus.CREATED)
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
