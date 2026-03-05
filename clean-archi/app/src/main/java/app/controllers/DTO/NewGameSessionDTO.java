package app.controllers.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "sessionId" })
public class NewGameSessionDTO {
    public String sessionId;

    public NewGameSessionDTO(String sessionId) {
        this.sessionId = sessionId;
    }
}
