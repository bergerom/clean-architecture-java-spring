package ports;

import java.io.IOException;

public interface CreateUser {
    CreateUserResponse createUser(CreateUserRequest createUserRequest) throws IOException;

    record CreateUserRequest(String name, Integer age) {
    }

    record CreateUserResponse(String id) {
    }
}
