package ma.streaming.upload.episode;

import ma.streaming.upload.episode.dto.EpisodeRequestDto;
import ma.streaming.upload.episode.dto.EpisodeResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public interface EpisodeService {
    EpisodeResponseDto createEpisode(EpisodeRequestDto episodeRequestDto) throws AlreadyExistsException, IOException;

    EpisodeResponseDto modifyEpisode(UUID episodeId, EpisodeRequestDto episodeRequestDto) throws AlreadyExistsException, NotFoundException, IOException;

    EpisodeResponseDto deleteEpisode(UUID episodeId) throws NotFoundException;

    Page<EpisodeResponseDto> findPaginatedEpisodes(int page, int size, String[] sort);

    EpisodeResponseDto findEpisodeById(UUID episodeId) throws NotFoundException;

    EpisodeResponseDto uploadEpisodeFile(UUID episodeId, MultipartFile episodeFile) throws ExecutionException, InterruptedException;

    List<EpisodeResponseDto> findAllEpisodesBySeasonId(UUID seasonId);
}
