package ma.streaming.upload.season;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.season.dto.SeasonRequestDto;
import ma.streaming.upload.season.dto.SeasonResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/seasons")
@Tag(name = "Season", description = "Endpoints for managing seasons in the database.")
public class SeasonController {
    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PostMapping
    @Operation(summary = "Create a new season", description = "This endpoint allows you to create a new season in the database.")
    @ApiResponse(responseCode = "201", description = "Season created successfully")
    public ResponseEntity<SeasonResponseDto> createSeason(@RequestBody SeasonRequestDto seasonRequestDto) throws AlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.seasonService.createSeason(seasonRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing season", description = "This endpoint allows you to modify an existing season in the database.")
    @ApiResponse(responseCode = "200", description = "Season modified successfully")
    public ResponseEntity<SeasonResponseDto> modifySeason(@PathVariable(value = "id") UUID seasonId,
                                                          @RequestBody SeasonRequestDto seasonRequestDto) throws AlreadyExistsException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.seasonService.modifySeason(seasonId, seasonRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a season", description = "This endpoint allows you to delete a season from the database.")
    @ApiResponse(responseCode = "204", description = "Season deleted successfully")
    public ResponseEntity<SeasonResponseDto> deleteSeason(@PathVariable(value = "id") UUID seasonId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.seasonService.deleteSeason(seasonId)
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get season by ID", description = "This endpoint allows you to fetch a season by its ID.")
    @ApiResponse(responseCode = "200", description = "Season fetched successfully")
    public ResponseEntity<SeasonResponseDto> findSeasonById(@PathVariable(value = "id") UUID seasonId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.seasonService.findSeasonById(seasonId)
        );
    }

    @GetMapping(path = "/series/{id}")
    @Operation(summary = "Get all seasons by series ID", description = "This endpoint allows you to fetch all seasons associated with a specific series ID.")
    @ApiResponse(responseCode = "200", description = "Seasons fetched successfully")
    @ApiResponse(responseCode = "204", description = "No seasons found for the given series ID")
    public ResponseEntity<List<SeasonResponseDto>> findAllSeasonsBySeriesId(@PathVariable(value = "id") UUID seriesId) {
        List<SeasonResponseDto> seasons = this.seasonService.findAllSeasonsBySeriesId(seriesId);
        if (seasons.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(seasons);
    }
}
