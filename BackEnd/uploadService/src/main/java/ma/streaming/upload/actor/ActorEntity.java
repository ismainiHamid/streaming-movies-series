package ma.streaming.upload.actor;

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
@Entity(name = "actors")
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @ManyToMany(mappedBy = "actors", fetch = FetchType.EAGER)
    private List<MovieEntity> movies;
    @ManyToMany(mappedBy = "actors", fetch = FetchType.EAGER)
    private List<SeriesEntity> series;
    @ManyToMany(mappedBy = "actors", fetch = FetchType.EAGER)
    private List<EpisodeEntity> episodes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorEntity that = (ActorEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
