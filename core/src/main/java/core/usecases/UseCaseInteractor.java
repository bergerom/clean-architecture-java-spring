package core.usecases;

import core.entities.GameScore;
import core.entities.User;
import core.export.dto.TotalScoreDTO;
import core.export.mappers.UserDTOMapper;
import core.ports.driving.AddScore;
import core.ports.driving.CreateUser;
import core.ports.driven.GameScoreRepository;
import core.ports.driving.GlobalScoreBoard;
import core.ports.driving.ListUsers;
import core.ports.driven.UserRepository;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class UseCaseInteractor implements CreateUser, ListUsers, GlobalScoreBoard, AddScore {

    private final UserRepository userRepository;
    private final GameScoreRepository gameScoreRepository;
    private final UserDTOMapper mapper;


    public UseCaseInteractor(UserRepository userRepository, GameScoreRepository gameScoreRepository) {
        this.userRepository = userRepository;
        this.gameScoreRepository = gameScoreRepository;
        this.mapper = Mappers.getMapper(UserDTOMapper.class);
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

    @Override
    public GlobalScoreBoardResponse getGlobalScoreBoard(GlobalScoreBoardRequest listUsersRequest) {
        List<User> users = userRepository.getUsers("");
        Map<UUID, List<GameScore>> uuidTotalScores = gameScoreRepository.getScoresForUser(users);

        List<TotalScoreDTO> totalScores = users.stream().map(user ->
                new TotalScoreDTO(
                        mapper.entityToDTO(user),
                        uuidTotalScores.get(user.userId())
                                .stream()
                                .mapToInt(GameScore::score).sum()
                )).collect(Collectors.toList());

        return new GlobalScoreBoardResponse(totalScores);
    }

    @Override
    public void addScoreForUser(AddScore.AddScoreRequest addScoreRequest) {
        UUID gameScoreId = UUID.randomUUID();

        GameScore score = new GameScore(gameScoreId,
                addScoreRequest.gameSessionId(),
                addScoreRequest.userId(),
                addScoreRequest.score(),
                addScoreRequest.date());

        gameScoreRepository.addScore(score);
    }
}
