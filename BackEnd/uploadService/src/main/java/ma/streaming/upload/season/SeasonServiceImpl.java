package ma.streaming.upload.season;

import ma.streaming.upload.season.dto.SeasonRequestDto;
import ma.streaming.upload.season.dto.SeasonResponseDto;
import ma.streaming.upload.season.mapper.SeasonMapper;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SeasonServiceImpl implements SeasonService {
    private final SeasonJpaRepository seasonJpaRepository;
    private final SeasonMapper seasonMapper;

    public SeasonServiceImpl(SeasonJpaRepository seasonJpaRepository, SeasonMapper seasonMapper) {
        this.seasonJpaRepository = seasonJpaRepository;
        this.seasonMapper = seasonMapper;
    }

    @Override
    @Transactional
    public SeasonResponseDto createSeason(SeasonRequestDto seasonRequestDto) throws AlreadyExistsException {
        if (this.seasonJpaRepository.existsByTitleIgnoreCaseAndNumberAndSeries_Id(seasonRequestDto.getTitle(), seasonRequestDto.getNumber(), seasonRequestDto.getSeriesId()))
            throw new AlreadyExistsException("The season with title: " + seasonRequestDto.getTitle() + ", number:" + seasonRequestDto.getNumber() + " And series id:" + seasonRequestDto.getSeriesId() + ", already exists");
        return this.seasonMapper.toSeasonResponseDto(
                this.seasonJpaRepository.saveAndFlush(
                        this.seasonMapper.toSeasonEntity(seasonRequestDto)
                )
        );
    }

    @Override
    @Transactional
    public SeasonResponseDto modifySeason(UUID seasonId, SeasonRequestDto seasonRequestDto) throws AlreadyExistsException, NotFoundException {
        SeasonEntity seasonEntity = this.seasonJpaRepository.findById(seasonId).orElseThrow(() ->
                new NotFoundException("Can't modify season with id:" + seasonId + ", dose not exist.")
        );

        if (!Objects.equals(seasonEntity.getNumber(), seasonRequestDto.getNumber()) || !seasonEntity.getTitle().equalsIgnoreCase(seasonRequestDto.getTitle()))
            if (this.seasonJpaRepository.existsByTitleIgnoreCaseAndNumberAndSeries_Id(seasonRequestDto.getTitle(), seasonRequestDto.getNumber(), seasonRequestDto.getSeriesId()))
                throw new AlreadyExistsException("The season with title: " + seasonRequestDto.getTitle() + ", number:" + seasonRequestDto.getNumber() + " And series id:" + seasonRequestDto.getSeriesId() + ", already exists");

        seasonEntity.setTitle(seasonRequestDto.getTitle());
        seasonEntity.setNumber(seasonRequestDto.getNumber());
        seasonEntity.setUpdatedAt(LocalDateTime.now());
        return this.seasonMapper.toSeasonResponseDto(seasonEntity);
    }

    @Override
    @Transactional
    public SeasonResponseDto deleteSeason(UUID seasonId) throws NotFoundException {
        SeasonEntity seasonEntity = this.seasonJpaRepository.findById(seasonId).orElseThrow(() ->
                new NotFoundException("Can't delete season with id:" + seasonId + ", dose not exist.")
        );
        this.seasonJpaRepository.delete(seasonEntity);
        return this.seasonMapper.toSeasonResponseDto(seasonEntity);
    }

    @Override
    public SeasonResponseDto findSeasonById(UUID seasonId) throws NotFoundException {
        return this.seasonMapper.toSeasonResponseDto(this.seasonJpaRepository.findById(seasonId).orElseThrow(() ->
                new NotFoundException("Can't find season with id:" + seasonId)
        ));
    }

    @Override
    public List<SeasonResponseDto> findAllSeasonsBySeriesId(UUID seriesId) {
        return this.seasonMapper.toSeasonResponseDtoList(
                this.seasonJpaRepository.findAllBySeries_Id(seriesId)
        );
    }
}
