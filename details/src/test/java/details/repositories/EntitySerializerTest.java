package details.repositories;

import core.entities.GameScore;
import core.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class EntitySerializerTest {

    @Test
    void unserializeUser() {
        String userLine = "d28917c5-dc5c-4316-ade4-b4780dbdb19a,jane,30";
        EntitySerializer<User> entitySerializer = new EntitySerializer<>(User.class);

        User actual = entitySerializer.parseLine(userLine);
        User expected = new User(UUID.fromString("d28917c5-dc5c-4316-ade4-b4780dbdb19a"), "jane", 30);

        Assertions
                .assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void unserializeGameScore() {
        String gameScoreLine = "fea7f5d9-ca3a-4101-ae00-7d1abb6d0562,711a13e4-781b-4e6a-983e-0edfac6b8bbb,f1028277-0ce3-45b5-8f36-826483e0ee02,5,2023-12-14T10:35:43.567270296Z";
        EntitySerializer<GameScore> entitySerializer = new EntitySerializer<>(GameScore.class);

        GameScore actual = entitySerializer.parseLine(gameScoreLine);
        GameScore expected = new GameScore(
                UUID.fromString("fea7f5d9-ca3a-4101-ae00-7d1abb6d0562"),
                UUID.fromString("711a13e4-781b-4e6a-983e-0edfac6b8bbb"),
                UUID.fromString("f1028277-0ce3-45b5-8f36-826483e0ee02"),
                5,
                Instant.parse("2023-12-14T10:35:43.567270296Z"));

        Assertions
                .assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void serializeUser() {
        UUID userId = UUID.randomUUID();
        String userName = "jane";
        Integer age = 30;
        User user = new User(userId, userName, age);

        EntitySerializer<User> entitySerializer = new EntitySerializer<>(User.class);

        String actual = entitySerializer.toCsvLine(user);
        String expected = String.join(",", List.of(userId.toString(), userName, age.toString()));

        Assertions
                .assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void serializeGameScore() {
        UUID gameScoreId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID gameSessionId = UUID.randomUUID();
        Integer score = 5;
        Instant instant = Instant.now();
        GameScore gameScore = new GameScore(gameScoreId, gameSessionId, userId, score, instant);

        EntitySerializer<GameScore> entitySerializer = new EntitySerializer<>(GameScore.class);

        String actual = entitySerializer.toCsvLine(gameScore);
        String expected = String.join(",",
                List.of(gameScoreId.toString(),
                        gameSessionId.toString(),
                        userId.toString(),
                        score.toString(),
                        instant.toString()));

        Assertions
                .assertThat(actual)
                .isEqualTo(expected);
    }
}