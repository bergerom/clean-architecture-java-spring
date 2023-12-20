import app.CleanArchitectureJavaSpringApplication;
import app.containers.AddScoreContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import core.export.dto.TotalScoreDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class ApiTests {

    CleanArchitectureJavaSpringApplication app = new CleanArchitectureJavaSpringApplication();
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeAll
    public static void initApp() {
        CleanArchitectureJavaSpringApplication.main(new String[]{});
    }

    // TODO : 1 - create user in initApp()
    //        2 - retrieve user uuid from the response of the previous request
    //        3 - create token for this user and send it in the subsequent request
    //        4 - verify score for this user is 15 + 5 = 20
    @Test
    public void getScoreBoardWithTwoScores() throws JsonProcessingException {
        UUID gameSessionUUID = UUID.randomUUID();

        AddScoreContainer score = createScore(gameSessionUUID, 15, Instant.now());
        postScore(score);

        score = createScore(gameSessionUUID, 5, Instant.now());
        postScore(score);

        TotalScoreDTO[] scores = getScores();
    }

    private AddScoreContainer createScore(UUID uuid, Integer score, Instant date) {
        return new AddScoreContainer(uuid, score, date);
    }

    private void postScore(AddScoreContainer score) throws JsonProcessingException {
        given()
                .contentType("application/json")
                .body(mapper.writeValueAsString(score))
                .when()
                .baseUri("http://localhost:8080/")
                .post("secure/score")
                .then()
                .statusCode(201);
    }

    private TotalScoreDTO[] getScores() {
        return given()
                .when()
                .baseUri("http://localhost:8080/")
                .get("/scores")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TotalScoreDTO[].class);

    }
}
