package details.repositories.utils;

import core.entities.User;
import details.repositories.UserFileRepository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileDatabaseTestUtils {
    private final Class<?> callingClass;

    public FileDatabaseTestUtils(Class<?> callingClass) {
        this.callingClass = callingClass;
    }

    public File initializeDatabaseFile(String fileName, List<User> users) throws IOException, URISyntaxException {
        File userTable = getResource(fileName);

        if (userTable.exists()) {
            Files.delete(userTable.toPath());
        }

        String fileContent = "";
        for (User user : users) {
            fileContent += UserFileRepository.serialize(user) + "\n";
        }

        Files.writeString(userTable.toPath(), fileContent, StandardOpenOption.CREATE);

        return userTable;
    }

    public File getResource(String resourceFileName) throws URISyntaxException {
        URL res = this.callingClass.getClassLoader().getResource(resourceFileName);
        return Paths.get(res.toURI()).toFile();
    }
}
