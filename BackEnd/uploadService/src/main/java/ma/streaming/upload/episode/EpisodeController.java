package ma.streaming.upload.episode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.episode.dto.EpisodeRequestDto;
import ma.streaming.upload.episode.dto.EpisodeResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/episodes")
@Tag(name = "Episode", description = "Endpoints for managing episodes in the streaming system")
public class EpisodeController {
    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @PostMapping
    @Operation(summary = "Create a new episode", description = "Creates a new episode in the system.")
    @ApiResponse(responseCode = "201", description = "Episode created successfully")
    public ResponseEntity<EpisodeResponseDto> createEpisode(@RequestBody EpisodeRequestDto episodeRequestDto) throws AlreadyExistsException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.episodeService.createEpisode(episodeRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing episode", description = "Modifies an episode with the given ID.")
    @ApiResponse(responseCode = "200", description = "Episode modified successfully")
    public ResponseEntity<EpisodeResponseDto> modifyEpisode(@PathVariable(value = "id") UUID episodeId,
                                                            @RequestBody EpisodeRequestDto episodeRequestDto) throws AlreadyExistsException, NotFoundException, IOException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.episodeService.modifyEpisode(episodeId, episodeRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete an episode by ID", description = "Deletes the episode with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Episode deleted successfully")
    public ResponseEntity<EpisodeResponseDto> deleteEpisode(@PathVariable(value = "id") UUID episodeId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.episodeService.deleteEpisode(episodeId)
        );
    }

    @GetMapping(path = "/paginated")
    @Operation(summary = "Get paginated list of episodes", description = "Fetches paginated episodes based on the provided parameters.")
    @ApiResponse(responseCode = "200", description = "Paginated episodes fetched successfully")
    public ResponseEntity<Page<EpisodeResponseDto>> findPaginatedEpisodes(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "title,asc") String[] sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.episodeService.findPaginatedEpisodes(page, size, sort)
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find episode by ID", description = "Fetches the episode details by ID.")
    @ApiResponse(responseCode = "200", description = "Episode fetched successfully")
    public ResponseEntity<EpisodeResponseDto> findEpisodeById(@PathVariable(value = "id") UUID episodeId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.episodeService.findEpisodeById(episodeId)
        );
    }

    @PostMapping(path = "/{id}/upload")
    @Operation(summary = "Upload an episode file", description = "Uploads the file for a specific episode.")
    @ApiResponse(responseCode = "201", description = "Episode file uploaded successfully")
    public ResponseEntity<EpisodeResponseDto> uploadEpisodeFile(@PathVariable(value = "id") UUID episodeId,
                                                                @RequestPart MultipartFile episodeFile) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.episodeService.uploadEpisodeFile(episodeId, episodeFile)
        );
    }

    @GetMapping(path = "/season/{id}")
    @Operation(summary = "Get all episodes by season ID", description = "This endpoint allows you to fetch all episodes associated with a specific season ID.")
    @ApiResponse(responseCode = "200", description = "Episodes fetched successfully")
    @ApiResponse(responseCode = "204", description = "No episodes found for the given season ID")
    public ResponseEntity<List<EpisodeResponseDto>> findAllEpisodesBySeasonId(@PathVariable(value = "id") UUID seasonId) {
        List<EpisodeResponseDto> episodes = this.episodeService.findAllEpisodesBySeasonId(seasonId);
        if (episodes.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(episodes);
    }
}
