package details.repositories;

import core.entities.User;
import details.repositories.utils.FileDatabaseTestUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;

public class UserFileRepositoryTest {

    FileDatabaseTestUtils fileDatabaseTestUtils = new FileDatabaseTestUtils(this.getClass());

    @Test
    public void testSaveUser() throws URISyntaxException, IOException {

        File userTable = fileDatabaseTestUtils.initializeDatabaseFile("users.csv", Collections.emptyList());
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(userTable.getParent());
        UserFileRepository userFileRepository = new UserFileRepository(coreFileDatabase);

        User user = new User(UUID.randomUUID(), "James", 23);
        userFileRepository.saveUser(user);

        String expectedFileContent = UserFileRepository.toComaSeparated(user);
        assertThat(linesOf(userTable).contains(expectedFileContent)).isTrue();

    }


    @Test
    public void testGetUsers() throws URISyntaxException, IOException {
        // Users table initial state
        List<User> users = List.of(
                new User(UUID.randomUUID(), "James", 23),
                new User(UUID.randomUUID(), "Jerry", 24),
                new User(UUID.randomUUID(), "Thomas", 30)
        );

        // Initalize file database with values
        File userTable = fileDatabaseTestUtils.initializeDatabaseFile("users.csv", users);
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(userTable.getParent());
        UserFileRepository userFileRepository = new UserFileRepository(coreFileDatabase);

        // Retrieve users whose name contains the letter "J"
        List<User> getUsersResponse = userFileRepository.getUsers("J");
        List<User> expectedUsers = users.stream().filter(u -> u.name().contains("J")).toList();

        assertThat(getUsersResponse.containsAll(expectedUsers)
                && getUsersResponse.size() == expectedUsers.size()).isTrue();

    }

    @Test
    public void testGetUser() throws URISyntaxException, IOException {
        // Users table initial state
        UUID specificUuid = UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3");
        List<User> users = List.of(
                new User(specificUuid, "James", 23),
                new User(UUID.randomUUID(), "Jerry", 24),
                new User(UUID.randomUUID(), "Thomas", 30)
        );

        File userTable = fileDatabaseTestUtils.initializeDatabaseFile("users.csv", users);
        CoreFileDatabase coreFileDatabase = new CoreFileDatabase(userTable.getParent());
        UserFileRepository userFileRepository = new UserFileRepository(coreFileDatabase);

        // Retrieve user with the specific UUID
        Optional<User> getUserResponse = userFileRepository.getUser(specificUuid);

        assertThat(getUserResponse.isPresent() && getUserResponse.get().userId().equals(specificUuid));

    }


}
