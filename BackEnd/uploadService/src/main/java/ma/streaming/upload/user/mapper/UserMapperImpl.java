package ma.streaming.upload.user.mapper;

import ma.streaming.upload.user.UserEntity;
import ma.streaming.upload.user.dto.UserRequestDto;
import ma.streaming.upload.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserEntity toUserEntity(UserRequestDto userRequestDto) {
        return UserEntity.builder()
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .build();
    }

    @Override
    public UserResponseDto toUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<UserResponseDto> toUserResponseDtoList(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(this::toUserResponseDto)
                .toList();
    }
}
