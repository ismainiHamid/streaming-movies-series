package ma.streaming.upload.user;

import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.user.dto.UserRequestDto;
import ma.streaming.upload.user.dto.UserResponseDto;
import ma.streaming.upload.user.mapper.UserMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws AlreadyExistsException {
        UserEntity userEntity = this.userMapper.toUserEntity(userRequestDto);
        if (this.userJpaRepository.existsByEmailAndDisabledAtIsNull(userEntity.getEmail()))
            throw new AlreadyExistsException("The user with email: " + userEntity.getEmail() + ", already exist.");
        if (this.userJpaRepository.existsByEmailAndDisabledAtNotNull(userEntity.getEmail()))
            throw new AlreadyExistsException("The user with email: " + userEntity.getEmail() + ", already exist, but his disabled.");
        return this.userMapper.toUserResponseDto(
                this.userJpaRepository.saveAndFlush(userEntity)
        );
    }

    @Override
    @Transactional
    public UserResponseDto modifyUser(UUID userId, UserRequestDto userRequestDto) throws AlreadyExistsException, NotFoundException {
        UserEntity userEdited = this.userMapper.toUserEntity(userRequestDto);
        UserEntity userEntity = this.userJpaRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Can't modify the user with id: " + userId + ", does not exist.")
        );

        if (!userEntity.getEmail().equals(userEdited.getEmail())) {
            if (this.userJpaRepository.existsByEmailAndDisabledAtIsNull(userEntity.getEmail()))
                throw new AlreadyExistsException("Can't modify the user with email: " + userEntity.getEmail() + ", already exist.");
            if (this.userJpaRepository.existsByEmailAndDisabledAtNotNull(userEntity.getEmail()))
                throw new AlreadyExistsException("Can't modify the user with email: " + userEntity.getEmail() + ", already exist, but his disabled.");
        }

        userEntity.setUsername(userEdited.getUsername());
        userEntity.setEmail(userEdited.getEmail());
        userEntity.setUpdatedAt(LocalDateTime.now());
        return this.userMapper.toUserResponseDto(userEntity);
    }

    @Override
    @Transactional
    public UserResponseDto deleteUser(UUID userId) throws NotFoundException {
        UserEntity userEntity = this.userJpaRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Can't delete the user with id: " + userId + ", does not exist.")
        );

        this.userJpaRepository.delete(userEntity);
        return this.userMapper.toUserResponseDto(userEntity);
    }

    @Override
    public Page<UserResponseDto> findPaginatedUsers(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));

        return new PageImpl<>(this.userMapper.toUserResponseDtoList(
                this.userJpaRepository.findAllBy(pageable)
        ), pageable, this.userJpaRepository.count());
    }

    @Override
    public UserResponseDto findUserById(UUID userId) throws NotFoundException {
        return this.userMapper.toUserResponseDto(this.userJpaRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Can't find the user with id: " + userId)
        ));
    }

    @Override
    @Transactional
    public UserResponseDto disableUser(UUID userId) throws NotFoundException {
        UserEntity userEntity = this.userJpaRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Can't disable the user with id: " + userId + ", does not exist.")
        );
        userEntity.setDisabledAt(LocalDateTime.now());
        return this.userMapper.toUserResponseDto(userEntity);
    }

    @Override
    public Page<UserResponseDto> findPaginatedUsersByEmail(int page, int size, String[] sort, String email) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));

        return new PageImpl<>(this.userMapper.toUserResponseDtoList(
                this.userJpaRepository.findAllByEmailContainingIgnoreCase(email, pageable)
        ), pageable, this.userJpaRepository.countAllByEmailContainingIgnoreCase(email));
    }
}
