package ma.streaming.upload.series;

import ma.streaming.upload.series.dto.SeriesRequestDto;
import ma.streaming.upload.series.dto.SeriesResponseDto;
import ma.streaming.upload.series.mapper.SeriesMapper;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.shared.utils.ImageUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class SeriesServiceImpl implements SeriesService {
    private final SeriesJpaRepository seriesJpaRepository;
    private final SeriesMapper seriesMapper;
    private final ImageUtil imageUtil;

    public SeriesServiceImpl(SeriesJpaRepository seriesJpaRepository, SeriesMapper seriesMapper, ImageUtil imageUtil) {
        this.seriesJpaRepository = seriesJpaRepository;
        this.seriesMapper = seriesMapper;
        this.imageUtil = imageUtil;
    }

    @Override
    @Transactional
    public SeriesResponseDto createSeries(SeriesRequestDto seriesRequestDto) throws AlreadyExistsException, IOException {
        SeriesEntity seriesEntity = this.seriesMapper.toEntity(seriesRequestDto);
        if (this.seriesJpaRepository.existsByTitleIgnoreCaseAndDeletedAtIsNull(seriesEntity.getTitle()))
            throw new AlreadyExistsException("The series with name:" + seriesEntity.getTitle() + ", already exists");
        seriesEntity.setPoster(this.imageUtil.processImage(seriesEntity.getPoster(),
                300,
                444,
                .85F
        ));
        seriesEntity.setThumbnail(this.imageUtil.processImage(seriesEntity.getThumbnail(),
                1920,
                1080,
                .85F
        ));
        return this.seriesMapper.toResponseDto(
                this.seriesJpaRepository.saveAndFlush(seriesEntity)
        );
    }

    @Override
    @Transactional
    public SeriesResponseDto modifySeries(UUID seriesId, SeriesRequestDto seriesRequestDto) throws AlreadyExistsException, NotFoundException, IOException {
        SeriesEntity seriesEdited = this.seriesMapper.toEntity(seriesRequestDto);
        SeriesEntity seriesEntity = this.seriesJpaRepository.findByIdAndDeletedAtIsNull(seriesId).orElseThrow(() ->
                new NotFoundException("Can't modify the series with id:" + seriesId + ", does not exist")
        );

        if (!seriesEntity.getTitle().equals(seriesEdited.getTitle()))
            if (this.seriesJpaRepository.existsByTitleIgnoreCaseAndDeletedAtIsNull(seriesEdited.getTitle()))
                throw new AlreadyExistsException("Can't modify series with title:" + seriesEdited.getTitle() + ", already exists");

        seriesEntity.setTitle(seriesEdited.getTitle());
        seriesEntity.setReleased(seriesEdited.getReleased());
        seriesEntity.setPlot(seriesEdited.getPlot());
        seriesEntity.setLanguage(seriesEdited.getLanguage());
        if (Objects.nonNull(seriesEdited.getPoster()))
            seriesEntity.setPoster(this.imageUtil.processImage(seriesEdited.getPoster(),
                    300,
                    444,
                    .85F
            ));
        if (Objects.nonNull(seriesEdited.getThumbnail()))
            seriesEntity.setThumbnail(this.imageUtil.processImage(seriesEntity.getThumbnail(),
                    1920,
                    1080,
                    .85F
            ));
        seriesEntity.setUpdatedAt(LocalDateTime.now());
        return this.seriesMapper.toResponseDto(seriesEntity);
    }

    @Override
    @Transactional
    public SeriesResponseDto deleteSeries(UUID seriesId) throws NotFoundException {
        SeriesEntity seriesEntity = this.seriesJpaRepository.findByIdAndDeletedAtIsNull(seriesId).orElseThrow(
                () -> new NotFoundException("Can't delete the series with id:" + seriesId + ", does not exist")
        );
        seriesEntity.setDeletedAt(LocalDateTime.now());
        return this.seriesMapper.toResponseDto(seriesEntity);
    }

    @Override
    public Page<SeriesResponseDto> findPaginateSeries(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.seriesMapper.toResponseDtoList(
                this.seriesJpaRepository.findAllByDeletedAtIsNull(pageable)
        ), pageable, this.seriesJpaRepository.countAllByDeletedAtIsNull());
    }

    @Override
    public SeriesResponseDto findSeriesById(UUID seriesId) throws NotFoundException {
        return this.seriesMapper.toResponseDto(this.seriesJpaRepository.findByIdAndDeletedAtIsNull(seriesId).orElseThrow(
                () -> new NotFoundException("Can't find the series with id:" + seriesId)
        ));
    }

    @Override
    public Page<SeriesResponseDto> findPaginatedSeriesByTitle(int page, int size, String[] sort, String title) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.seriesMapper.toResponseDtoList(
                this.seriesJpaRepository.findAllByDeletedAtIsNullAndTitleContainingIgnoreCase(title, pageable)
        ), pageable, this.seriesJpaRepository.countAllByDeletedAtIsNullAndTitleContainingIgnoreCase(title));
    }
}
