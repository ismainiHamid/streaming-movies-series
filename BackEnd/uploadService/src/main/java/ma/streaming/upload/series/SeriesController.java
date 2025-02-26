package ma.streaming.upload.series;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.series.dto.SeriesRequestDto;
import ma.streaming.upload.series.dto.SeriesResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/series")
@Tag(name = "Series", description = "Endpoints for managing series in the database.")
public class SeriesController {
    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping
    @Operation(summary = "Create a new series", description = "Creates a new series using the provided SeriesRequestDto object.")
    @ApiResponse(responseCode = "201", description = "Series created successfully.")
    public ResponseEntity<SeriesResponseDto> createSeries(@ModelAttribute SeriesRequestDto seriesRequestDto) throws AlreadyExistsException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.seriesService.createSeries(seriesRequestDto)
        );
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "Modify an existing series", description = "Modifies an existing series identified by the seriesId using the provided SeriesRequestDto object.")
    @ApiResponse(responseCode = "200", description = "Series modified successfully.")
    public ResponseEntity<SeriesResponseDto> modifySeries(@PathVariable(value = "id") UUID seriesId,
                                                          @ModelAttribute SeriesRequestDto seriesRequestDto) throws AlreadyExistsException, NotFoundException, IOException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.seriesService.modifySeries(seriesId, seriesRequestDto)
        );
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a series", description = "Deletes a series identified by the seriesId.")
    @ApiResponse(responseCode = "204", description = "Series deleted successfully.")
    public ResponseEntity<SeriesResponseDto> deleteSeries(@PathVariable(value = "id") UUID seriesId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.seriesService.deleteSeries(seriesId)
        );
    }

    @GetMapping(path = "/paginated")
    @Operation(summary = "Get paginated list of series", description = "Retrieves a paginated list of series with sorting options.")
    @ApiResponse(responseCode = "200", description = "List of series retrieved successfully.")
    public ResponseEntity<Page<SeriesResponseDto>> findPaginatedSeries(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(defaultValue = "title,asc") String[] sort) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.seriesService.findPaginateSeries(page, size, sort)
        );
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get series by ID", description = "Retrieves series details by seriesId.")
    @ApiResponse(responseCode = "200", description = "Series details retrieved successfully.")
    public ResponseEntity<SeriesResponseDto> findSeriesById(@PathVariable(value = "id") UUID seriesId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.seriesService.findSeriesById(seriesId)
        );
    }

    @GetMapping(path = "/filter")
    @Operation(summary = "Get paginated series episodes by title", description = "Retrieves a paginated list of series episodes based on the provided title.")
    @ApiResponse(responseCode = "200", description = "List of series episodes by title retrieved successfully.")
    public ResponseEntity<Page<SeriesResponseDto>> findPaginatedSeriesByTitle(@RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size,
                                                                              @RequestParam(defaultValue = "title,asc") String[] sort,
                                                                              @RequestParam String title) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.seriesService.findPaginatedSeriesByTitle(page, size, sort, title)
        );
    }
}
