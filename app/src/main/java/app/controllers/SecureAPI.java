package app.controllers;

import app.containers.AddScoreContainer;
import app.containers.CreateSessionContainer;
import app.controllers.DTO.NewGameSessionDTO;
import app.security.UserToken;
import core.ports.driving.AddScore;
import core.ports.driving.CreateGameSession;
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
        UUID userId = userToken.getUserId(); // TODO : replace with real token retrieval
        AddScore.AddScoreRequest addScoreRequest = new AddScore.AddScoreRequest(
                userId,
                score.gameSessionId(),
                score.score(),
                score.date()
        );

        useCaseInteractor.addScoreForUser(addScoreRequest);
    }

    @PostMapping("/secure/session")
    @ResponseStatus(HttpStatus.CREATED)
    public NewGameSessionDTO newGameSessionForUser() {
        UUID userId = userToken.getUserId(); // TODO : replace with real token retrieval

        CreateGameSession.CreateGameSessionRequest createGameSessionRequest
                = new CreateGameSession.CreateGameSessionRequest(userId);

        UUID sessionId = useCaseInteractor.createGameSession(createGameSessionRequest);

        return new NewGameSessionDTO(sessionId.toString());
    }
}
