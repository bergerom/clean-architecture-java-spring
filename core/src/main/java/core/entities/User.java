package core.entities;

import java.io.Serializable;
import java.util.UUID;

public record User(UUID userId, String name, Integer age) implements Serializable {}
