package ma.streaming.upload.subtitle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.streaming.upload.episode.EpisodeEntity;
import ma.streaming.upload.movie.MovieEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "subtitles")
public class SubtitleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String path;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne(cascade = CascadeType.ALL)
    private MovieEntity movie;
    @ManyToOne(cascade = CascadeType.ALL)
    private EpisodeEntity episode;
}
