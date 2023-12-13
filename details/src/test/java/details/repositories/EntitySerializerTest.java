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

        User actual = entitySerializer.unserialize(userLine);
        User expected = new User(UUID.fromString("d28917c5-dc5c-4316-ade4-b4780dbdb19a"), "jane", 30);

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

        String actual = entitySerializer.serialize(user);
        String expected = String.join(",", List.of(userId.toString(), userName, age.toString()));

        Assertions
                .assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void serializeGameScore() {
        UUID userId = UUID.randomUUID();
        UUID gameSessionId = UUID.randomUUID();
        Integer score = 5;
        Instant instant = Instant.now();
        GameScore gameScore = new GameScore(gameSessionId, userId, score, instant);

        EntitySerializer<GameScore> entitySerializer = new EntitySerializer<>(GameScore.class);

        String actual = entitySerializer.serialize(gameScore);
        String expected = String.join(",",
                List.of(gameSessionId.toString(), userId.toString(), score.toString(), instant.toString()));

        Assertions
                .assertThat(actual)
                .isEqualTo(expected);
    }
}