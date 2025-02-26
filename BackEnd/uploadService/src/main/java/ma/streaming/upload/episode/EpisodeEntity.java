package ma.streaming.upload.episode;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.season.SeasonEntity;
import ma.streaming.upload.series.SeriesEntity;
import ma.streaming.upload.shared.enums.StatusEnum;
import ma.streaming.upload.subtitle.SubtitleEntity;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "episodes")
@EqualsAndHashCode(callSuper = true)
public class EpisodeEntity extends SeriesEntity {
    @Column(nullable = false)
    private String runtime;
    @Column(nullable = false)
    private String director;
    @Column()
    private String masterFile;
    @Column()
    private StatusEnum status;
    @ManyToOne(cascade = CascadeType.ALL)
    private SeasonEntity season;
    @OneToMany(mappedBy = "episode", fetch = FetchType.EAGER)
    private List<SubtitleEntity> subtitles;
}
