package ma.streaming.upload.user;

import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.user.dto.UserRequestDto;
import ma.streaming.upload.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto) throws AlreadyExistsException;

    UserResponseDto modifyUser(UUID userId, UserRequestDto userRequestDto) throws AlreadyExistsException, NotFoundException;

    UserResponseDto deleteUser(UUID userId) throws NotFoundException;

    Page<UserResponseDto> findPaginatedUsers(int page, int size, String[] sort);

    UserResponseDto findUserById(UUID userId) throws NotFoundException;

    UserResponseDto disableUser(UUID userId) throws NotFoundException;

    Page<UserResponseDto> findPaginatedUsersByEmail(int page, int size, String[] sort, String email);
}
