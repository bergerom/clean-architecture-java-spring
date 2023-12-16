package app.security;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public interface UserToken {
    default UUID getUserId() {
        List<UUID> userIds = List.of(
                UUID.fromString("fea7f5d9-ca3a-4101-ae00-7d1abb6d0562"),
                UUID.fromString("711a13e4-781b-4e6a-983e-0edfac6b8bbb"),
                UUID.fromString("f1028277-0ce3-45b5-8f36-826483e0ee02"));
        Random random = new Random();
        return userIds.get(random.nextInt(3));
    }
}
