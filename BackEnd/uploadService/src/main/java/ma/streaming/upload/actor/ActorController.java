package ma.streaming.upload.actor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.actor.dto.ActorRequestDto;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/actors")
@Tag(name = "Actor", description = "Endpoints for managing actors in the streaming application")
public class ActorController {
    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    @Operation(summary = "Create a new actor", description = "Creates a new actor in the system.")
    @ApiResponse(responseCode = "201", description = "Actor created successfully")
    public ResponseEntity<ActorResponseDto> createActor(@RequestBody ActorRequestDto actorRequestDto) throws AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.actorService.createdActor(actorRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing actor", description = "Modifies an actor with the given ID.")
    @ApiResponse(responseCode = "200", description = "Actor modified successfully")
    public ResponseEntity<ActorResponseDto> modifyActor(@PathVariable(value = "id") UUID actorId, @RequestBody ActorRequestDto actorRequestDto) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.actorService.modifyActor(actorId, actorRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete an actor by ID", description = "Deletes the actor with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Actor deleted successfully")
    public ResponseEntity<ActorResponseDto> deleteActor(@PathVariable(value = "id") UUID actorId) throws NotFoundException, BadRequestException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.actorService.deleteActor(actorId)
        );
    }

    @GetMapping(path = "/paginated")
    @Operation(summary = "Get paginated list of actors", description = "Fetches paginated actors based on the provided parameters.")
    @ApiResponse(responseCode = "200", description = "Paginated actors fetched successfully")
    public ResponseEntity<Page<ActorResponseDto>> findPaginatedActors(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "name,asc") String[] sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.actorService.findPaginatedActors(page, size, sort)
        );
    }

    @GetMapping
    @Operation(summary = "Get all actors", description = "Fetches all the actors in the system.")
    @ApiResponse(responseCode = "200", description = "All actors fetched successfully")
    public ResponseEntity<List<ActorResponseDto>> findAllActors() {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.actorService.findAllActors()
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find actor by ID", description = "Fetches the actor details by ID.")
    @ApiResponse(responseCode = "200", description = "Actor fetched successfully")
    public ResponseEntity<ActorResponseDto> findActorById(@PathVariable(value = "id") UUID actorId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.actorService.findActorById(actorId)
        );
    }

    @GetMapping(path = "/filter")
    @Operation(summary = "Filter actors by name", description = "Fetches actors filtered by the provided name.")
    @ApiResponse(responseCode = "200", description = "Actors filtered by name fetched successfully")
    public ResponseEntity<Page<ActorResponseDto>> filterActorByName(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "name,asc") String[] sort,
                                                                    @RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.actorService.findPaginatedActorByName(page, size, sort, name)
        );
    }

    @GetMapping(path = "/{name}/exist")
    @Operation(summary = "Check if an actor with the name exists", description = "Checks if any actor with the given name exists in the system.")
    @ApiResponse(responseCode = "200", description = "Actor existence checked successfully")
    @ApiResponse(responseCode = "400", description = "Bad request: Name must not be null or empty")
    public ResponseEntity<Map<String, Object>> existsActorByName(@PathVariable String name) throws NotFoundException {
        if (name == null || name.trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error",
                            "message", "Name must not be null or empty"));

        boolean exists = this.actorService.existsActorByName(name.trim());
        return ResponseEntity.ok(Map.of("status", "success",
                "data", Map.of("exists", exists)));
    }
}
