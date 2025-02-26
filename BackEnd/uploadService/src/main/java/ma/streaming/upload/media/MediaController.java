package ma.streaming.upload.media;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import ma.streaming.upload.media.dto.MediaResponseDto;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1/medias")
@CrossOrigin(value = "http://localhost:4200")
@Tag(name = "Media", description = "Endpoints for managing media, including posters, genres, actors, and pagination based on title.")
public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping(path = "/{id}/poster")
    @Operation(summary = "Retrieve media poster by movie ID", description = "Fetches the poster image for a given movie by its ID.")
    @ApiResponse(responseCode = "200", description = "Media poster fetched successfully")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<byte[]> produceMediaPosterById(@PathVariable(value = "id") UUID mediaId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(this.mediaService.produceMediaPosterById(mediaId));
    }

    @GetMapping(path = "/{id}/thumbnail")
    @Operation(summary = "Retrieve media thumbnail by media ID", description = "Fetches the thumbnail image for a given movie by its ID.")
    @ApiResponse(responseCode = "200", description = "Media poster fetched successfully")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<byte[]> produceMediaThumbnailById(@PathVariable(value = "id") UUID mediaId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(this.mediaService.produceThumbnailPosterById(mediaId));
    }

    @PatchMapping(path = "/{id}/genres")
    @Operation(summary = "Add genres to media", description = "Associates genres to a specific media item using media ID.")
    @ApiResponse(responseCode = "201", description = "Genres added to media successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request, genre IDs list is empty")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<Void> addGenresToMedia(@PathVariable(value = "id") UUID mediaId,
                                                 @RequestBody List<UUID> genreIds) throws NotFoundException {
        if (Objects.isNull(genreIds) || genreIds.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        this.mediaService.addGenresToMedia(mediaId, genreIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{id}/genres")
    @Operation(summary = "Remove genres from media", description = "Disassociates genres from a specific media item using media ID.")
    @ApiResponse(responseCode = "204", description = "Genres removed from media successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request, genre IDs list is empty")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<Void> removeGenresFromMedia(@PathVariable(value = "id") UUID mediaId,
                                                      @RequestParam(value = "genreIds") List<UUID> genreIds) throws NotFoundException {
        if (Objects.isNull(genreIds) || genreIds.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        this.mediaService.removeGenresFromMedia(mediaId, genreIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{id}/genres")
    @Operation(summary = "Fetch genres associated with media", description = "Retrieves the genres linked to a specific media item using media ID.")
    @ApiResponse(responseCode = "200", description = "Genres retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<List<GenreResponseDto>> getGenresByMediaId(@PathVariable(value = "id") UUID mediaId) throws NotFoundException {
        List<GenreResponseDto> genres = this.mediaService.getGenresByMediaId(mediaId);
        return genres.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.status(HttpStatus.OK).body(genres);
    }

    @PatchMapping(path = "/{id}/actors")
    @Operation(summary = "Add actors to media", description = "Associates actors to a specific media item using media ID.")
    @ApiResponse(responseCode = "201", description = "Actors added to media successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request, actor IDs list is empty")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<Void> addActorsToMedia(@PathVariable(value = "id") UUID mediaId,
                                                 @RequestBody List<UUID> actorIds) throws NotFoundException {
        if (Objects.isNull(actorIds) || actorIds.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        this.mediaService.addActorsToMedia(mediaId, actorIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{id}/actors")
    @Operation(summary = "Remove actors from media", description = "Disassociates actors from a specific media item using media ID.")
    @ApiResponse(responseCode = "204", description = "Actors removed from media successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request, actor IDs list is empty")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<Void> removeActorsFromMedia(@PathVariable(value = "id") UUID mediaId,
                                                      @RequestParam(value = "actorIds") List<UUID> actorIds) throws NotFoundException {
        if (Objects.isNull(actorIds) || actorIds.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        this.mediaService.removeActorsFromMedia(mediaId, actorIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path = "/{id}/actors")
    @Operation(summary = "Fetch actors associated with media", description = "Retrieves the actors linked to a specific media item using media ID.")
    @ApiResponse(responseCode = "200", description = "Actors retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<List<ActorResponseDto>> getActorsByMediaId(@PathVariable(value = "id") UUID mediaId) throws NotFoundException {
        List<ActorResponseDto> actors = this.mediaService.getActorsByMediaId(mediaId);
        return actors.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.status(HttpStatus.OK).body(actors);
    }

    @GetMapping(path = "/filter")
    @Operation(summary = "Fetch paginated list of media by title", description = "Retrieves a paginated list of media items based on the title.")
    @ApiResponse(responseCode = "200", description = "Media list fetched successfully")
    public ResponseEntity<Page<MediaResponseDto>> findPaginatedMediaByTitle(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam(defaultValue = "title,asc") String[] sort,
                                                                            @RequestParam String title) {
        return ResponseEntity.status(HttpStatus.OK).body(this.mediaService.findPaginatedMediaByTitle(page, size, sort, title));
    }

    @GetMapping(path = "/recommendation")
    public ResponseEntity<List<MediaResponseDto>> recommendationByGenres(@RequestParam List<String> genres) throws NotFoundException {
        if (Objects.isNull(genres) || genres.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        List<MediaResponseDto> mediaResponseDtos = this.mediaService.recommendationByGenres(genres);
        if (mediaResponseDtos.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).body(mediaResponseDtos);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get the master file by media ID", description = "Fetches the master file associated with the given media ID. Returns the master file content or an error if not found.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the master file")
    public ResponseEntity<Map<String, Object>> getMasterFileByMediaId(@PathVariable(value = "id") UUID mediaId) throws NotFoundException, BadRequestException {
        Map<String, Object> masterFile = this.mediaService.getMasterFileByMediaId(mediaId);
        if (masterFile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).body(masterFile);
    }

    @GetMapping(path = "/watch/{segment}")
    @Operation(summary = "Stream media segment", description = "Streams a specific HLS media segment or master playlist stored in MinIO. Supports HTTP Range requests for efficient streaming.")
    @ApiResponse(responseCode = "200", description = "Media streamed successfully")
    public ResponseEntity<InputStreamResource> watchMedia(@PathVariable(value = "segment") String segment,
                                                          @RequestHeader(value = "Range", required = false) String range) throws Exception {
        return this.mediaService.watchMedia(segment, range);
    }
}
