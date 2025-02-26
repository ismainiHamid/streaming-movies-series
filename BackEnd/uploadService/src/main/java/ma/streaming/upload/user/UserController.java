package ma.streaming.upload.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.user.dto.UserRequestDto;
import ma.streaming.upload.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/users")
@Tag(name = "User", description = "Endpoints for managing users.")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid request or user already exists")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) throws AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.userService.createUser(userRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing user", description = "Modifies the details of a user by ID")
    @ApiResponse(responseCode = "200", description = "User successfully modified")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid request or user already exists")
    public ResponseEntity<UserResponseDto> modifyUser(@PathVariable(value = "id") UUID userId,
                                                      @RequestBody UserRequestDto userRequestDto) throws AlreadyExistsException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.modifyUser(userId, userRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete an existing user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable(value = "id") UUID userId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.userService.deleteUser(userId)
        );
    }

    @GetMapping(path = "/paginated")
    @Operation(summary = "Retrieve a paginated list of users", description = "Gets a paginated list of users with sorting options")
    @ApiResponse(responseCode = "200", description = "Paginated list of users successfully retrieved")
    public ResponseEntity<Page<UserResponseDto>> findPaginatedUsers(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "email, asc") String[] sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.findPaginatedUsers(page, size, sort)
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Retrieve user by ID", description = "Gets the details of a user by their unique ID")
    @ApiResponse(responseCode = "200", description = "User successfully retrieved")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable(value = "id") UUID userId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.findUserById(userId)
        );
    }

    @PatchMapping(path = "/{id}/disable")
    @Operation(summary = "Disable a user", description = "Disables a user by their unique ID")
    @ApiResponse(responseCode = "204", description = "User successfully disabled")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponseDto> disableUser(@PathVariable(value = "id") UUID userId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.userService.disableUser(userId)
        );
    }

    @GetMapping(path = "/filter")
    @Operation(summary = "Retrieve a paginated list of users by email", description = "Fetches a paginated list of users based on the provided email")
    @ApiResponse(responseCode = "200", description = "Paginated list of users successfully retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    public ResponseEntity<Page<UserResponseDto>> findPaginatedUsersByEmail(@RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestParam(defaultValue = "email, asc") String[] sort,
                                                                           @RequestParam String email){
        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.findPaginatedUsersByEmail(page, size, sort, email)
        );

    }
}
