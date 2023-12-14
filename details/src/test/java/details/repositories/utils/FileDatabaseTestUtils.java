package details.repositories.utils;

import core.entities.GameScore;
import core.entities.User;

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

    public File initializeDatabaseFile(String fileName, List<Record> records) throws IOException, URISyntaxException {
        // TODO : do not rely on resources
        File table = getResource(fileName);

        if (table.exists()) {
            Files.delete(table.toPath());
        }

        String fileContent = "";
        for (Record record : records) {
            fileContent += serialize(record) + "\n";
        }

        Files.writeString(table.toPath(), fileContent, StandardOpenOption.CREATE);

        return table;
    }

    public File getResource(String resourceFileName) throws URISyntaxException {
        URL res = this.callingClass.getClassLoader().getResource(resourceFileName);
        return Paths.get(res.toURI()).toFile();
    }

    public static String serialize(Record obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.userId()
                    + ","
                    + user.name()
                    + ","
                    + user.age();
        } else if (obj instanceof GameScore) {
            GameScore gameScore = (GameScore) obj;
            return String.join(",",
                    gameScore.gameScoreId().toString(),
                    gameScore.gameSessionId().toString(),
                    gameScore.userId().toString(),
                    gameScore.score().toString(),
                    gameScore.date().toString());
        }

        return "";
    }
}
