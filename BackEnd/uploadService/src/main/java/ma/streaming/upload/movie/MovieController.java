package ma.streaming.upload.movie;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.movie.dto.MovieRequestDto;
import ma.streaming.upload.movie.dto.MovieResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/movies")
@Tag(name = "Movie", description = "Endpoints for managing movies, including creation, modification, deletion, file uploads, and retrieval.")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Creates a new movie using the provided MovieRequestDto object.")
    @ApiResponse(responseCode = "201", description = "Movie created successfully.")
    public ResponseEntity<MovieResponseDto> createMovie(@ModelAttribute MovieRequestDto movieRequestDto) throws AlreadyExistsException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.movieService.createMovie(movieRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing movie", description = "Modifies an existing movie identified by the movieId using the provided MovieRequestDto object.")
    @ApiResponse(responseCode = "200", description = "Movie modified successfully.")
    public ResponseEntity<MovieResponseDto> modifyMovie(@PathVariable(value = "id") UUID movieId,
                                                        @ModelAttribute MovieRequestDto movieRequestDto) throws AlreadyExistsException, NotFoundException, IOException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.movieService.modifyMovie(movieId, movieRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a movie", description = "Deletes a movie identified by the movieId.")
    @ApiResponse(responseCode = "204", description = "Movie deleted successfully.")
    public ResponseEntity<MovieResponseDto> deleteMovie(@PathVariable(value = "id") UUID movieId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.movieService.deleteMovie(movieId)
        );
    }

    @GetMapping(path = "/paginated")
    @Operation(summary = "Get paginated list of movies", description = "Retrieves a paginated list of movies with sorting options.")
    @ApiResponse(responseCode = "200", description = "List of movies retrieved successfully.")
    public ResponseEntity<Page<MovieResponseDto>> findPaginatedMovies(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "title,asc") String[] sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.movieService.findPaginatedMovies(page, size, sort)
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieves movie details by movieId.")
    @ApiResponse(responseCode = "200", description = "Movie details retrieved successfully.")
    public ResponseEntity<MovieResponseDto> findMovieById(@PathVariable(value = "id") UUID movieId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.movieService.findMovieById(movieId)
        );
    }

    @GetMapping(path = "/filter")
    @Operation(summary = "Get paginated movies by title", description = "Retrieves a paginated list of movies based on the provided title.")
    @ApiResponse(responseCode = "200", description = "List of movies by title retrieved successfully.")
    public ResponseEntity<Page<MovieResponseDto>> findPaginatedMoviesByTitle(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "title,asc") String[] sort,
                                                                             @RequestParam String title) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.movieService.findPaginatedMoviesByTitle(page, size, sort, title)
        );
    }

    @PostMapping(path = "/{id}/upload")
    @Operation(summary = "Upload movie file", description = "Uploads a movie file associated with a specific movie identified by movieId.")
    @ApiResponse(responseCode = "201", description = "Movie file uploaded successfully.")
    public ResponseEntity<MovieResponseDto> uploadMovieFile(@PathVariable(value = "id") UUID movieId,
                                                            @RequestPart MultipartFile movieFile) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.movieService.uploadMovieFile(movieId, movieFile)
        );
    }
}
