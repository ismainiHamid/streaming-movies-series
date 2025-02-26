package ma.streaming.upload.genre;

import jakarta.persistence.*;
import lombok.*;
import ma.streaming.upload.episode.EpisodeEntity;
import ma.streaming.upload.movie.MovieEntity;
import ma.streaming.upload.series.SeriesEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "genres")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    private List<MovieEntity> movies;
    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    private List<EpisodeEntity> episodes;
    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    private List<SeriesEntity> series;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreEntity that = (GenreEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
