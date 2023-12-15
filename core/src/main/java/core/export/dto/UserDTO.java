package core.export.dto;

import java.io.Serializable;
import java.util.UUID;

public record UserDTO(UUID userId, String name, Integer age) {};

