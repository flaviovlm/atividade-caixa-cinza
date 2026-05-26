package application.mapper;

import application.user.dto.UserRequestDTO;
import application.user.dto.UserResponseDTO;
import application.user.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO (UserModel userModel);
    UserModel toModel (UserRequestDTO dto);
}
