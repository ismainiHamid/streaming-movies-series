package ma.streaming.upload.subtitle;

import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.subtitle.dto.SubtitleRequestDto;
import ma.streaming.upload.subtitle.dto.SubtitleResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubtitleService {
    SubtitleResponseDto createSubtitle(SubtitleRequestDto subtitleRequestDto);

    SubtitleResponseDto modifySubtitle(UUID subtitleId, SubtitleRequestDto subtitleRequestDto);

    SubtitleResponseDto deleteSubtitle(UUID subtitleId);

    List<SubtitleResponseDto> findAllSubtitlesByMovieId(UUID movieId) throws NotFoundException;

    List<SubtitleResponseDto> findAllSubtitlesByEpisodeId(UUID episodeId) throws NotFoundException;

    SubtitleResponseDto findSubtitleById(UUID subtitleId) throws NotFoundException;
}
