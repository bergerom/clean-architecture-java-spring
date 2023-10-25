package entities;

import java.util.UUID;

public record User(UUID uuid, String name, Integer age, Integer score) {}
