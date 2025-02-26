package ma.streaming.upload.media;

import ma.streaming.upload.actor.ActorEntity;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import ma.streaming.upload.actor.mapper.ActorMapper;
import ma.streaming.upload.episode.EpisodeEntity;
import ma.streaming.upload.genre.GenreEntity;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import ma.streaming.upload.genre.mapper.GenreMapper;
import ma.streaming.upload.media.dto.MediaResponseDto;
import ma.streaming.upload.media.mapper.MediaMapper;
import ma.streaming.upload.movie.MovieEntity;
import ma.streaming.upload.series.SeriesEntity;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.shared.storage.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Service
public class MediaServiceImpl implements MediaService {
    private final MediaJpaRepository mediaJpaRepository;
    private final MinioService minioService;
    private final MediaMapper mediaMapper;
    private final ActorMapper actorMapper;
    private final GenreMapper genreMapper;

    public MediaServiceImpl(MediaJpaRepository mediaJpaRepository, MinioService minioService, MediaMapper mediaMapper, ActorMapper actorMapper, GenreMapper genreMapper) {
        this.mediaJpaRepository = mediaJpaRepository;
        this.minioService = minioService;
        this.mediaMapper = mediaMapper;
        this.actorMapper = actorMapper;
        this.genreMapper = genreMapper;
    }

    @Override
    public byte[] produceMediaPosterById(UUID mediaId) throws NotFoundException {
        return this.mediaJpaRepository.findById(mediaId).orElseThrow(() ->
                new NotFoundException("Can't produce the poster for media with id: " + mediaId + ", dose not exist.")
        ).getPoster();
    }

    @Override
    public byte[] produceThumbnailPosterById(UUID mediaId) throws NotFoundException {
        return this.mediaJpaRepository.findById(mediaId).orElseThrow(() ->
                new NotFoundException("Can't produce the thumbnail for media with id: " + mediaId + ", dose not exist.")
        ).getThumbnail();
    }

    @Override
    @Transactional
    public void addGenresToMedia(UUID mediaId, List<UUID> genreIds) throws NotFoundException {
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't find the media with id: " + mediaId)
        );

        genreIds.forEach(genreId -> {
            GenreEntity genreEntity = GenreEntity.builder().id(genreId).build();
            if (!mediaEntity.getGenres().contains(genreEntity))
                mediaEntity.getGenres().add(genreEntity);
        });
    }

    @Override
    @Transactional
    public void removeGenresFromMedia(UUID mediaId, List<UUID> genreIds) throws NotFoundException {
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't find the media with id: " + mediaId)
        );

        genreIds.forEach(genreId -> {
            GenreEntity genreEntity = GenreEntity.builder().id(genreId).build();
            mediaEntity.getGenres().remove(genreEntity);
        });
    }

    @Override
    public List<GenreResponseDto> getGenresByMediaId(UUID mediaId) throws NotFoundException {
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't find the media with id: " + mediaId)
        );

        return this.genreMapper.toGenreResponseDtoList(mediaEntity.getGenres());
    }

    @Override
    @Transactional
    public void addActorsToMedia(UUID mediaId, List<UUID> actorIds) throws NotFoundException {
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't find the media with id: " + mediaId)
        );

        actorIds.forEach(actorId -> {
            ActorEntity actorEntity = ActorEntity.builder().id(actorId).build();
            if (!mediaEntity.getActors().contains(actorEntity))
                mediaEntity.getActors().add(actorEntity);
        });
    }

    @Override
    @Transactional
    public void removeActorsFromMedia(UUID mediaId, List<UUID> actorIds) throws NotFoundException {
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't find the media with id: " + mediaId)
        );

        actorIds.forEach(actorId -> {
            ActorEntity actorEntity = ActorEntity.builder().id(actorId).build();
            mediaEntity.getActors().remove(actorEntity);
        });
    }

    @Override
    public List<ActorResponseDto> getActorsByMediaId(UUID mediaId) throws NotFoundException {
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't find the media with id: " + mediaId)
        );

        return this.actorMapper.toActorResponseDtoList(mediaEntity.getActors());
    }

    @Override
    public Page<MediaResponseDto> findPaginatedMediaByTitle(int page, int size, String[] sort, String title) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));

        List<MediaEntity> medias = this.mediaJpaRepository.findAllByDeletedAtIsNullAndTitleIsContainingIgnoreCase(title, pageable);
        List<MediaResponseDto> mediaResponseDtos = medias.stream().map(media -> {
            MediaResponseDto mediaResponseDto = this.mediaMapper.toMediaResponseDto(media);
            if (media instanceof MovieEntity) {
                mediaResponseDto.setType("Movie");
            } else if (media instanceof SeriesEntity) {
                mediaResponseDto.setType("Series");
            }
            return mediaResponseDto;
        }).toList();

        return new PageImpl<>(mediaResponseDtos, pageable,
                this.mediaJpaRepository.countAllByDeletedAtIsNullAndTitleIsContainingIgnoreCase(title));
    }

    @Override
    public List<MediaResponseDto> recommendationByGenres(List<String> genres) throws NotFoundException {
        List<MediaEntity> mediaEntities = new ArrayList<>();
        Set<UUID> mediaIds = new HashSet<>();
        genres.forEach(genre -> this.mediaJpaRepository.findAllByGenres_NameContainingIgnoreCase(genre)
                .stream()
                .filter(media -> media instanceof MovieEntity || media instanceof SeriesEntity)
                .limit(9)
                .toList().forEach(media -> {
                    if (!mediaIds.contains(media.getId())) {
                        mediaIds.add(media.getId());
                        mediaEntities.add(media);
                    }
                })
        );
        return this.mediaMapper.toMediaResponseDtoList(mediaEntities);
    }

    @Override
    public Map<String, Object> getMasterFileByMediaId(UUID mediaId) throws NotFoundException, BadRequestException {
        Map<String, Object> masterFile = new HashMap<>();
        MediaEntity mediaEntity = this.mediaJpaRepository.findByIdAndDeletedAtIsNull(mediaId).orElseThrow(() ->
                new NotFoundException("Can't begin the stream because the segment does not exist.")
        );
        if (mediaEntity instanceof MovieEntity)
            masterFile.put("segment", ((MovieEntity) mediaEntity).getMasterFile());
        else if (mediaEntity instanceof EpisodeEntity)
            masterFile.put("segment", ((EpisodeEntity) mediaEntity).getMasterFile());

        if (masterFile.isEmpty())
            throw new BadRequestException("The media would you try to streaming not such a media.");
        return masterFile;
    }

    @Override
    public ResponseEntity<InputStreamResource> watchMedia(String segment, String range) throws Exception {
        if (!segment.endsWith(".ts") && !segment.endsWith(".m3u8"))
            throw new BadRequestException("The segment format invalid");

        String contentType = segment.endsWith(".m3u8") ? "application/vnd.apple.mpegurl" : "video/MP2T";

        long fileSize = this.minioService.getObjectSize(segment);
        long rangeStart = 0;
        long rangeEnd = fileSize - 1;
        if (range != null && !range.isEmpty()) {
            try {
                String[] ranges = range.replace("bytes=", "").split("-");
                rangeStart = Long.parseLong(ranges[0]);

                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    rangeEnd = Long.parseLong(ranges[1]);
                }

                if (rangeStart < 0 || rangeEnd >= fileSize || rangeStart > rangeEnd) {
                    return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                            .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                            .build();
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                        .build();
            }
        }
        long rangeLength = rangeEnd - rangeStart + 1;
        InputStream inputStream = this.minioService.getObjectWithRange(
                segment,
                rangeStart,
                rangeEnd
        );
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", rangeStart, rangeEnd, fileSize))
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                .body(new InputStreamResource(inputStream));
    }
}
