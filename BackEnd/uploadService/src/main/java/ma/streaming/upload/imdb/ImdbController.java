package ma.streaming.upload.imdb;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.imdb.dto.ImdbResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/imdb")
@Tag(name = "IMDB", description = "Endpoints for fetching media details from IMDb")
public class ImdbController {
    private final ImdbService imdbService;

    public ImdbController(ImdbService imdbService) {
        this.imdbService = imdbService;
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get media details from IMDb", description = "Fetches media details using the IMDb ID.")
    @ApiResponse(responseCode = "200", description = "Media details fetched successfully")
    @ApiResponse(responseCode = "404", description = "Media not found")
    public ResponseEntity<ImdbResponse> getMediaDetails(@PathVariable(value = "id") String imdbId) {
        ImdbResponse imdbResponse = this.imdbService.getMediaDetails(imdbId);

        if (imdbResponse.getResponse().equals("False"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(imdbResponse);
        return ResponseEntity.status(HttpStatus.OK).body(imdbResponse);
    }
}
