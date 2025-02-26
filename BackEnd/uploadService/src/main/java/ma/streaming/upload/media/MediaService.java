package ma.streaming.upload.media;

import ma.streaming.upload.actor.dto.ActorResponseDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import ma.streaming.upload.media.dto.MediaResponseDto;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface MediaService {
    byte[] produceMediaPosterById(UUID mediaId) throws NotFoundException;

    byte[] produceThumbnailPosterById(UUID mediaId) throws NotFoundException;

    void addGenresToMedia(UUID mediaId, List<UUID> genreIds) throws NotFoundException;

    void removeGenresFromMedia(UUID mediaId, List<UUID> genreIds) throws NotFoundException;

    List<GenreResponseDto> getGenresByMediaId(UUID mediaId) throws NotFoundException;

    void addActorsToMedia(UUID mediaId, List<UUID> actorIds) throws NotFoundException;

    void removeActorsFromMedia(UUID mediaId, List<UUID> actorIds) throws NotFoundException;

    List<ActorResponseDto> getActorsByMediaId(UUID mediaId) throws NotFoundException;

    Page<MediaResponseDto> findPaginatedMediaByTitle(int page, int size, String[] sort, String title);

    List<MediaResponseDto> recommendationByGenres(List<String> genres) throws NotFoundException;

    Map<String, Object> getMasterFileByMediaId(UUID mediaId) throws NotFoundException, BadRequestException;

    ResponseEntity<InputStreamResource> watchMedia(String segment, String range) throws Exception;
}
