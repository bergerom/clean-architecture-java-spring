package core.usecases;

import core.entities.User;
import core.ports.CreateUser;
import core.ports.GameScoreRepository;
import core.ports.ListUsers;
import core.ports.UserRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UseCaseInteractor implements CreateUser, ListUsers {

    private final UserRepository userRepository;
    private final GameScoreRepository gameScoreRepository;


    public UseCaseInteractor(UserRepository userRepository, GameScoreRepository gameScoreRepository) {
        this.userRepository = userRepository;
        this.gameScoreRepository = gameScoreRepository;
    }

    @Override
    public CreateUser.CreateUserResponse createUser(CreateUser.CreateUserRequest createUserRequest) {

        User user = new User(UUID.randomUUID(),
                createUserRequest.name(),
                createUserRequest.age());

        userRepository.saveUser(user);
        // TODO : validate business rules for user

        return new CreateUserResponse(user.userId().toString());
    }

    @Override
    public ListUserResponse listUsers(ListUserRequest listUsersRequest) {
        List<User> users = userRepository.getUsers(listUsersRequest.nameContains());

        return new ListUserResponse(users);
    }
}
