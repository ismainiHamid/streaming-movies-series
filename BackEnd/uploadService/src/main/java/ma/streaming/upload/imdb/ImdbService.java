package ma.streaming.upload.imdb;

import ma.streaming.upload.imdb.dto.ImdbResponse;

public interface ImdbService {
    ImdbResponse getMediaDetails(String imdbId);
}
