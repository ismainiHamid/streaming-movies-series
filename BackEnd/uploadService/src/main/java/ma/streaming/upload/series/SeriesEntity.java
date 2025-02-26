package ma.streaming.upload.series;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.media.MediaEntity;
import ma.streaming.upload.season.SeasonEntity;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity(name = "series")
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class SeriesEntity extends MediaEntity {
    @OneToMany(mappedBy = "series", fetch = FetchType.EAGER)
    private List<SeasonEntity> seasons;
}
