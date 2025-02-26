package ma.streaming.upload.role.mapper;

import ma.streaming.upload.role.RoleEntity;
import ma.streaming.upload.role.dto.RoleRequestDto;
import ma.streaming.upload.role.dto.RoleResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    RoleEntity toRoleEntity(RoleRequestDto roleRequestDto);

    RoleResponseDto toRoleResponseDto(RoleEntity roleEntity);

    List<RoleResponseDto> toRoleResponseDtoList(List<RoleEntity> roleEntities);
}
