package ma.streaming.upload.subtitle;

import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.subtitle.dto.SubtitleRequestDto;
import ma.streaming.upload.subtitle.dto.SubtitleResponseDto;
import ma.streaming.upload.subtitle.mapper.SubtitleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubtitleServiceImpl implements SubtitleService {
    private final SubtitleJpaRepository subtitleJpaRepository;
    private final SubtitleMapper subtitleMapper;

    public SubtitleServiceImpl(SubtitleJpaRepository subtitleJpaRepository, SubtitleMapper subtitleMapper) {
        this.subtitleJpaRepository = subtitleJpaRepository;
        this.subtitleMapper = subtitleMapper;
    }

    @Override
    public SubtitleResponseDto createSubtitle(SubtitleRequestDto subtitleRequestDto) {
        return null;
    }

    @Override
    public SubtitleResponseDto modifySubtitle(UUID subtitleId, SubtitleRequestDto subtitleRequestDto) {
        return null;
    }

    @Override
    public SubtitleResponseDto deleteSubtitle(UUID subtitleId) {
        return null;
    }

    @Override
    public List<SubtitleResponseDto> findAllSubtitlesByMovieId(UUID movieId) throws NotFoundException {
        return List.of();
    }

    @Override
    public List<SubtitleResponseDto> findAllSubtitlesByEpisodeId(UUID episodeId) throws NotFoundException {
        return List.of();
    }

    @Override
    public SubtitleResponseDto findSubtitleById(UUID subtitleId) throws NotFoundException {
        return null;
    }
}
