package ports;


import entities.User;

import java.io.IOException;

public interface UserRepository {
    void saveUser(User user) throws IOException;
}
