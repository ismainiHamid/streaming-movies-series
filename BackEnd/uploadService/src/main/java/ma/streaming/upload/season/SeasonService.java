package ma.streaming.upload.season;

import ma.streaming.upload.season.dto.SeasonRequestDto;
import ma.streaming.upload.season.dto.SeasonResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeasonService {
    SeasonResponseDto createSeason(SeasonRequestDto seasonRequestDto) throws AlreadyExistsException;

    SeasonResponseDto modifySeason(UUID seasonId, SeasonRequestDto seasonRequestDto) throws AlreadyExistsException, NotFoundException;

    SeasonResponseDto deleteSeason(UUID seasonId) throws NotFoundException;

    SeasonResponseDto findSeasonById(UUID seasonId) throws NotFoundException;

    List<SeasonResponseDto> findAllSeasonsBySeriesId(UUID seriesId);
}
