package details.repositories;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;


public class CoreFileDatabase {
    private final String databasePath;

    public CoreFileDatabase(String databasePath) {
        this.databasePath = databasePath;
    }

    public Stream<String> readAllLines(FileDatabaseTable table) {

        Path fullFilePath = Paths.get(this.databasePath, table.toString());

        if (!Files.exists(fullFilePath)) {
            return Stream.empty();
        }

        try {
            return Files.readAllLines(fullFilePath).stream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    public void addNewLine(FileDatabaseTable table, String line) {
        Path fullFilePath = Paths.get(this.databasePath, table.toString());
        try {
            if (!Files.exists(fullFilePath)) {
                Files.createFile(fullFilePath);
            }

            Files.writeString(fullFilePath,
                    line + "\n",
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
