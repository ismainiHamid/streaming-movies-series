package ma.streaming.upload.user.mapper;

import ma.streaming.upload.user.UserEntity;
import ma.streaming.upload.user.dto.UserRequestDto;
import ma.streaming.upload.user.dto.UserResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    UserEntity toUserEntity(UserRequestDto userRequestDto);

    UserResponseDto toUserResponseDto(UserEntity userEntity);

    List<UserResponseDto> toUserResponseDtoList(List<UserEntity> userEntities);
}
