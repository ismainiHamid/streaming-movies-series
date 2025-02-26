package ma.streaming.upload.role;

import ma.streaming.upload.role.dto.RoleRequestDto;
import ma.streaming.upload.role.dto.RoleResponseDto;
import ma.streaming.upload.role.mapper.RoleMapper;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleJpaRepository roleJpaRepository, RoleMapper roleMapper) {
        this.roleJpaRepository = roleJpaRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) throws AlreadyExistsException {
        RoleEntity roleEntity = this.roleMapper.toRoleEntity(roleRequestDto);
        if (this.roleJpaRepository.existsByNameIgnoreCase(roleEntity.getName()))
            throw new AlreadyExistsException("The role with name:" + roleEntity.getName() + " already exist.");
        return this.roleMapper.toRoleResponseDto(
                this.roleJpaRepository.saveAndFlush(roleEntity)
        );
    }

    @Override
    @Transactional
    public RoleResponseDto modifyRole(UUID roleId, RoleRequestDto roleRequestDto) throws AlreadyExistsException, NotFoundException {
        RoleEntity roleEdited = this.roleMapper.toRoleEntity(roleRequestDto);
        RoleEntity roleEntity = this.roleJpaRepository.findById(roleId).orElseThrow(() ->
                new NotFoundException("Can't modify the role with id:" + roleId + ", does not exist.")
        );

        if (!roleEntity.getName().equalsIgnoreCase(roleRequestDto.getName()))
            if (this.roleJpaRepository.existsByNameIgnoreCase(roleEntity.getName()))
                throw new AlreadyExistsException("Can't modify the role with name:" + roleEntity.getName() + " already exist.");

        roleEntity.setName(roleEdited.getName());
        roleEntity.setDescription(roleEdited.getDescription());
        roleEntity.setUpdatedAt(LocalDateTime.now());
        return this.roleMapper.toRoleResponseDto(roleEntity);
    }

    @Override
    @Transactional
    public RoleResponseDto deleteRole(UUID roleId) throws NotFoundException, BadRequestException {
        RoleEntity roleEntity = this.roleJpaRepository.findById(roleId).orElseThrow(() ->
                new NotFoundException("Can't delete the role with id:" + roleId + ", does not exist.")
        );

        if (!roleEntity.getUsers().isEmpty())
            throw new BadRequestException("Can't delete the role with id:" + roleId + ", because there are users.");
        this.roleJpaRepository.delete(roleEntity);
        return this.roleMapper.toRoleResponseDto(roleEntity);
    }

    @Override
    public List<RoleResponseDto> findAllRoles() {
        return this.roleMapper.toRoleResponseDtoList(
                this.roleJpaRepository.findAll()
        );
    }

    @Override
    public RoleResponseDto findRoleById(UUID roleId) {
        return this.roleMapper.toRoleResponseDto(
                this.roleJpaRepository.findById(roleId).orElseThrow(() ->
                        new NotFoundException("Can't find the role with id:" + roleId)
                )
        );
    }
}
