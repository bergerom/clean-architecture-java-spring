package usecases;

import entities.User;
import ports.CreateUser;
import ports.UserRepository;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class UseCaseInteractor implements CreateUser {

    private final UserRepository userRepository;

    public UseCaseInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CreateUser.CreateUserResponse createUser(CreateUser.CreateUserRequest createUserRequest) throws IOException {

        User user = new User(UUID.randomUUID(),
                createUserRequest.name(),
                createUserRequest.age(),
                new Random().nextInt(10));

        userRepository.saveUser(user);
        // TODO : validate business rules for user

        return new CreateUserResponse(user.uuid().toString());
    }
}
