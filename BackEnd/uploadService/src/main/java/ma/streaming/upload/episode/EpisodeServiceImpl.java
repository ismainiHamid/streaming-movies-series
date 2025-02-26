package ma.streaming.upload.episode;

import ma.streaming.upload.episode.dto.EpisodeRequestDto;
import ma.streaming.upload.episode.dto.EpisodeResponseDto;
import ma.streaming.upload.episode.mapper.EpisodeMapper;
import ma.streaming.upload.shared.enums.StatusEnum;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.shared.utils.ConvertingUtil;
import ma.streaming.upload.shared.utils.ImageUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    private final EpisodeJpaRepository episodeJpaRepository;
    private final EpisodeMapper episodeMapper;
    private final ImageUtil imageUtil;
    private final ConvertingUtil ffmpegUtil;

    public EpisodeServiceImpl(EpisodeJpaRepository episodeJpaRepository, EpisodeMapper episodeMapper, ImageUtil imageUtil, ConvertingUtil ffmpegUtil) {
        this.episodeJpaRepository = episodeJpaRepository;
        this.episodeMapper = episodeMapper;
        this.imageUtil = imageUtil;
        this.ffmpegUtil = ffmpegUtil;
    }

    @Override
    @Transactional
    public EpisodeResponseDto createEpisode(EpisodeRequestDto episodeRequestDto) throws AlreadyExistsException, IOException {
        EpisodeEntity episodeEntity = this.episodeMapper.toEntity(episodeRequestDto);
        if (this.episodeJpaRepository.existsByTitleIgnoreCaseAndDeletedAtIsNull(episodeEntity.getTitle()))
            throw new AlreadyExistsException("The episode with title:" + episodeEntity.getTitle() + ", already exist.");
        episodeEntity.setPoster(this.imageUtil.processImage(episodeEntity.getPoster(),
                300,
                444,
                .85F
        ));
        return this.episodeMapper.toResponseDto(
                this.episodeJpaRepository.saveAndFlush(episodeEntity)
        );
    }

    @Override
    @Transactional
    public EpisodeResponseDto modifyEpisode(UUID episodeId, EpisodeRequestDto episodeRequestDto) throws AlreadyExistsException, NotFoundException, IOException {
        EpisodeEntity episodeEdited = this.episodeMapper.toEntity(episodeRequestDto);
        EpisodeEntity episodeEntity = this.episodeJpaRepository.findByIdAndDeletedAtIsNull(episodeId).orElseThrow(() ->
                new NotFoundException("Can't modify episode with id:" + episodeId + ", does not exist.")
        );

        if (!episodeEntity.getTitle().equals(episodeEdited.getTitle()))
            if (this.episodeJpaRepository.existsByTitleIgnoreCaseAndDeletedAtIsNull(episodeEdited.getTitle()))
                throw new AlreadyExistsException("Can't modify episode with title:" + episodeEdited.getTitle() + ", already exist.");

        episodeEntity.setTitle(episodeEdited.getTitle());
        episodeEntity.setReleased(episodeEdited.getReleased());
        episodeEntity.setRuntime(episodeEdited.getRuntime());
        episodeEntity.setDirector(episodeEdited.getDirector());
        episodeEntity.setPlot(episodeEdited.getPlot());
        episodeEntity.setLanguage(episodeEdited.getLanguage());
        if (Objects.nonNull(episodeEdited.getPoster()))
            episodeEntity.setPoster(this.imageUtil.processImage(episodeEdited.getPoster(),
                    300,
                    444,
                    .85F
            ));
        episodeEntity.setUpdatedAt(LocalDateTime.now());
        return this.episodeMapper.toResponseDto(episodeEntity);
    }

    @Override
    @Transactional
    public EpisodeResponseDto deleteEpisode(UUID episodeId) throws NotFoundException {
        EpisodeEntity episodeEntity = this.episodeJpaRepository.findByIdAndDeletedAtIsNull(episodeId).orElseThrow(() ->
                new NotFoundException("Can't delete episode with id:" + episodeId + ", does not exist.")
        );
        episodeEntity.setDeletedAt(LocalDateTime.now());
        return this.episodeMapper.toResponseDto(episodeEntity);
    }

    @Override
    public Page<EpisodeResponseDto> findPaginatedEpisodes(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.episodeMapper.toResponseDtoList(
                this.episodeJpaRepository.findAllByDeletedAtIsNull(pageable)
        ), pageable, this.episodeJpaRepository.countAllByDeletedAtIsNull());
    }

    @Override
    public EpisodeResponseDto findEpisodeById(UUID episodeId) throws NotFoundException {
        return this.episodeMapper.toResponseDto(this.episodeJpaRepository.findByIdAndDeletedAtIsNull(episodeId).orElseThrow(() ->
                new NotFoundException("Can't find episode with id:" + episodeId)
        ));
    }

    @Override
    @Transactional
    public EpisodeResponseDto uploadEpisodeFile(UUID episodeId, MultipartFile episodeFile) throws ExecutionException, InterruptedException {
        EpisodeEntity episodeEntity = this.episodeJpaRepository.findByIdAndDeletedAtIsNull(episodeId).orElseThrow(() ->
                new NotFoundException("Can't upload episode whit id:" + episodeId + ", because dose not exist.")
        );

        String masterFile = this.ffmpegUtil.uploadAndConvertFile(episodeEntity.getId(), episodeFile).get();
        episodeEntity.setMasterFile(masterFile);
        episodeEntity.setStatus(StatusEnum.COMPLETED);
        return this.episodeMapper.toResponseDto(episodeEntity);
    }

    @Override
    public List<EpisodeResponseDto> findAllEpisodesBySeasonId(UUID seasonId) {
        return this.episodeMapper.toResponseDtoList(
                this.episodeJpaRepository.findAllByDeletedAtIsNullAndSeason_Id(seasonId)
        );
    }
}
