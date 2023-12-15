package core.export.mappers;

import core.entities.User;
import core.export.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserDTOMapper {
    UserDTO entityToDTO(User user);

}
