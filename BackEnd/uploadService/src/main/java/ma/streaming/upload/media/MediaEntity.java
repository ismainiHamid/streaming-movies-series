package ma.streaming.upload.media;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.actor.ActorEntity;
import ma.streaming.upload.genre.GenreEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "medias")
@Inheritance(strategy = InheritanceType.JOINED)
public class MediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String released;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String plot;
    @Column(nullable = false)
    private String language;
    @Column(nullable = false)
    private byte[] poster;
    @Column(nullable = false)
    private byte[] thumbnail;
    @Column(nullable = false)
    private String imdbRating;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<ActorEntity> actors;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<GenreEntity> genres;
}
