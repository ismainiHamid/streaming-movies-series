package ma.streaming.upload.genre;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.genre.dto.GenreRequestDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
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
@RequestMapping(path = "/v1/genres")
@Tag(name = "Genre", description = "Endpoints for managing genres in the streaming system")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    @Operation(summary = "Create a new genre", description = "Creates a new genre in the system.")
    @ApiResponse(responseCode = "201", description = "Genre created successfully")
    public ResponseEntity<GenreResponseDto> createGenre(@RequestBody GenreRequestDto genreRequestDto) throws AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.genreService.createGenre(genreRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing genre", description = "Modifies a genre with the given ID.")
    @ApiResponse(responseCode = "200", description = "Genre modified successfully")
    public ResponseEntity<GenreResponseDto> modifyGenre(@PathVariable(value = "id") UUID genreId,
                                                        @RequestBody GenreRequestDto genreRequestDto) throws AlreadyExistsException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.genreService.modifyGenre(genreId, genreRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a genre by ID", description = "Deletes the genre with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Genre deleted successfully")
    public ResponseEntity<GenreResponseDto> deleteGenre(@PathVariable(value = "id") UUID genreId) throws NotFoundException, BadRequestException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.genreService.deleteGenre(genreId)
        );
    }

    @GetMapping(path = "/paginated")
    @Operation(summary = "Get paginated list of genres", description = "Fetches paginated genres based on the provided parameters.")
    @ApiResponse(responseCode = "200", description = "Paginated genres fetched successfully")
    public ResponseEntity<Page<GenreResponseDto>> findPaginatedGenres(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "name,asc") String[] sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.genreService.findPaginatedGenres(page, size, sort)
        );
    }

    @GetMapping
    @Operation(summary = "Fetch all genres", description = "Fetches all genres from the system.")
    @ApiResponse(responseCode = "200", description = "All genres fetched successfully")
    public ResponseEntity<List<GenreResponseDto>> findAllGenres() {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.genreService.findAllGenres()
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find genre by ID", description = "Fetches the genre details by ID.")
    @ApiResponse(responseCode = "200", description = "Genre fetched successfully")
    public ResponseEntity<GenreResponseDto> findGenreById(@PathVariable(value = "id") UUID genreId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.genreService.findGenreById(genreId)
        );
    }

    @GetMapping(path = "/filter")
    @Operation(summary = "Filter genres by name", description = "Fetches genres that match the provided name.")
    @ApiResponse(responseCode = "200", description = "Genres filtered by name successfully")
    public ResponseEntity<Page<GenreResponseDto>> findPaginatedGenreByName(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "name,asc") String[] sort,
                                                                    @RequestParam String genreName) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.genreService.findPaginatedGenreByName(page, size, sort, genreName)
        );
    }

    @GetMapping(path = "/{name}/exist")
    @Operation(summary = "Check if a genre by name exists", description = "Checks if a genre with the given name exists in the system.")
    @ApiResponse(responseCode = "200", description = "Check if genre by name exists successfully")
    public ResponseEntity<Map<String, Object>> existsGenreByName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error",
                            "message", "Name must not be null or empty"));

        boolean exists = this.genreService.existsGenreByName(name.trim());
        return ResponseEntity.ok(Map.of("status", "success",
                "data", Map.of("exists", exists)));
    }
}
