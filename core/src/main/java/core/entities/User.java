package core.entities;

import java.util.UUID;

public record User(UUID userId, String name, Integer age) {}
