package core.export.dto;

import java.util.UUID;

public record UserDTO(UUID userId, String name, Integer age) {}

