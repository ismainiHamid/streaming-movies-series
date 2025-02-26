package ma.streaming.upload.role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.role.dto.RoleRequestDto;
import ma.streaming.upload.role.dto.RoleResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/roles")
@Tag(name = "Role", description = "Endpoints for managing roles in the system.")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @Operation(summary = "Create a new role", description = "This endpoint allows you to create a new role in the system.")
    @ApiResponse(responseCode = "201", description = "Role created successfully")
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleRequestDto roleRequestDto) throws AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.roleService.createRole(roleRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing role", description = "This endpoint allows you to modify the details of an existing role using its ID.")
    @ApiResponse(responseCode = "200", description = "Role modified successfully")
    public ResponseEntity<RoleResponseDto> modifyRole(@PathVariable("id") UUID roleId, @RequestBody RoleRequestDto roleRequestDto) throws AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.roleService.modifyRole(roleId, roleRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a role", description = "This endpoint allows you to delete a role from the system using its ID.")
    @ApiResponse(responseCode = "200", description = "Role deleted successfully")
    public ResponseEntity<RoleResponseDto> deleteRole(@PathVariable("id") UUID roleId) throws NotFoundException, BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.roleService.deleteRole(roleId)
        );
    }

    @GetMapping
    @Operation(summary = "Find all roles", description = "This endpoint retrieves a list of all roles available in the system.")
    @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    public ResponseEntity<List<RoleResponseDto>> findAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.roleService.findAllRoles()
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find a role by ID", description = "This endpoint retrieves the details of a specific role using its unique ID.")
    @ApiResponse(responseCode = "200", description = "Role retrieved successfully")
    public ResponseEntity<RoleResponseDto> findRoleById(@PathVariable("id") UUID roleId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.roleService.findRoleById(roleId)
        );
    }
}
