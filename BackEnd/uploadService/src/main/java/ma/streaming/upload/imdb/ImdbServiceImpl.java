package ma.streaming.upload.imdb;

import ma.streaming.upload.imdb.dto.ImdbResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImdbServiceImpl implements ImdbService {
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://www.omdbapi.com/?apikey=%s&i=%s";
    private static final String API_KEY = "23cf897f";

    public ImdbServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ImdbResponse getMediaDetails(String imdbId) {
        return this.restTemplate.getForObject(
                String.format(API_URL, API_KEY, imdbId),
                ImdbResponse.class
        );
    }
}
