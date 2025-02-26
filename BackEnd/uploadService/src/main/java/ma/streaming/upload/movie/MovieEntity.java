package ma.streaming.upload.movie;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.media.MediaEntity;
import ma.streaming.upload.shared.enums.StatusEnum;
import ma.streaming.upload.subtitle.SubtitleEntity;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "movies")
@EqualsAndHashCode(callSuper = true)
public class MovieEntity extends MediaEntity {
    @Column(nullable = false)
    private String runtime;
    @Column(nullable = false)
    private String director;
    @Column()
    private String masterFile;
    @Column()
    private StatusEnum status;
    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private List<SubtitleEntity> subtitles;
}
