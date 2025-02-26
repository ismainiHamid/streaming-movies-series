package ma.streaming.upload.role.mapper;

import ma.streaming.upload.role.RoleEntity;
import ma.streaming.upload.role.dto.RoleRequestDto;
import ma.streaming.upload.role.dto.RoleResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleEntity toRoleEntity(RoleRequestDto roleRequestDto) {
        return RoleEntity.builder()
                .name(roleRequestDto.getName())
                .description(roleRequestDto.getDescription())
                .build();
    }

    @Override
    public RoleResponseDto toRoleResponseDto(RoleEntity roleEntity) {
        return RoleResponseDto.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .description(roleEntity.getDescription())
                .createdAt(roleEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<RoleResponseDto> toRoleResponseDtoList(List<RoleEntity> roleEntities) {
        return roleEntities.stream()
                .map(this::toRoleResponseDto)
                .toList();
    }
}
