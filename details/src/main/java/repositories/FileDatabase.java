package repositories;

import entities.User;
import ports.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileDatabase implements UserRepository {

    public String databasePath;
    private static final String USER_TABLE_NAME = "users.csv";

    public FileDatabase(String databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public void saveUser(User user) throws IOException {

        Path fullFilePath = Paths.get(this.databasePath, USER_TABLE_NAME);

        if (!Files.exists(fullFilePath)) {
            Files.createFile(fullFilePath);
        }

        Files.writeString(fullFilePath,
                toComaSeparated(user),
                StandardOpenOption.APPEND);
    }

    private String toComaSeparated(User user) {
        return user.uuid()
                + ","
                + user.name()
                + ","
                + user.age()
                + ","
                + user.score();
    }
}
