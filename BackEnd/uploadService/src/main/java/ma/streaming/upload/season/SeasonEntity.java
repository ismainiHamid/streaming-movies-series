package ma.streaming.upload.season;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.streaming.upload.episode.EpisodeEntity;
import ma.streaming.upload.series.SeriesEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "seasons")
public class SeasonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer number;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @ManyToOne(cascade = CascadeType.ALL)
    private SeriesEntity series;
    @OneToMany(mappedBy = "season", fetch = FetchType.EAGER)
    private List<EpisodeEntity> episodes;
}
