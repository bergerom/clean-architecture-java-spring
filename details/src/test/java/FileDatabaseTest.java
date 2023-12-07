
import entities.User;
import org.junit.jupiter.api.Test;
import repositories.FileDatabase;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;

public class FileDatabaseTest {
    @Test
    public void testSaveUser() throws URISyntaxException, IOException {

        File userTable = initializeDatabaseFile("users.csv");
        FileDatabase fileDatabase = new FileDatabase(userTable.getParent());
        User user = new User(UUID.randomUUID(), "James", 23, 10);

        fileDatabase.saveUser(user);

        assertThat(linesOf(userTable).containsAll(
                List.of("1, james, 23, 10")
        ));

    }

    private File initializeDatabaseFile(String fileName) throws IOException, URISyntaxException {
        File userTable = getResource(fileName);
        if (userTable.exists()) {
            Files.delete(userTable.toPath());
        }
        return userTable;
    }

    private File getResource(String resourceFileName) throws URISyntaxException {
        URL res = getClass().getClassLoader().getResource(resourceFileName);
        return Paths.get(res.toURI()).toFile();
    }
}
