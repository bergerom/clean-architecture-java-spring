package core.ports;

import core.entities.User;

import java.io.IOException;
import java.util.List;

public interface ListUsers {
    ListUsers.ListUserResponse listUsers(ListUsers.ListUserRequest listUsersRequest) throws IOException;

    record ListUserRequest(String nameContains) {
    }

    record ListUserResponse(List<User> users) {
    }
}
