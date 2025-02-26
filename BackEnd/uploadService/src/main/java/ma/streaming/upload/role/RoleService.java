package ma.streaming.upload.role;

import ma.streaming.upload.role.dto.RoleRequestDto;
import ma.streaming.upload.role.dto.RoleResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleService {
    RoleResponseDto createRole(RoleRequestDto roleRequestDto) throws AlreadyExistsException;

    RoleResponseDto modifyRole(UUID roleId, RoleRequestDto roleRequestDto) throws AlreadyExistsException, NotFoundException;

    RoleResponseDto deleteRole(UUID roleId) throws NotFoundException, BadRequestException;

    List<RoleResponseDto> findAllRoles();

    RoleResponseDto findRoleById(UUID roleId);
}
