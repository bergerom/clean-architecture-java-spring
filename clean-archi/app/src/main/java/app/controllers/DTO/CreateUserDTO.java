package app.controllers.DTO;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "userId" })
public class CreateUserDTO {
    public String userId;

    public CreateUserDTO(String userId) {
        this.userId = userId;
    }
}
