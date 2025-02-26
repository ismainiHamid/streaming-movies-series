package ma.streaming.upload.season;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeasonJpaRepository extends JpaRepository<SeasonEntity, UUID> {
    List<SeasonEntity> findAllBySeries_Id(UUID seriesId);

    boolean existsByTitleIgnoreCaseAndNumberAndSeries_Id(String title, Integer number, UUID seriesId);
}
