package ma.streaming.upload.series;

import ma.streaming.upload.series.dto.SeriesRequestDto;
import ma.streaming.upload.series.dto.SeriesResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.UUID;

@Repository
public interface SeriesService {
    SeriesResponseDto createSeries(SeriesRequestDto seriesRequestDto) throws AlreadyExistsException, IOException;

    SeriesResponseDto modifySeries(UUID seriesId, SeriesRequestDto seriesRequestDto) throws AlreadyExistsException, NotFoundException, IOException;

    SeriesResponseDto deleteSeries(UUID seriesId) throws NotFoundException;

    Page<SeriesResponseDto> findPaginateSeries(int page, int size, String[] sort);

    SeriesResponseDto findSeriesById(UUID seriesId) throws NotFoundException;

    Page<SeriesResponseDto> findPaginatedSeriesByTitle(int page, int size, String[] sort, String title);
}
