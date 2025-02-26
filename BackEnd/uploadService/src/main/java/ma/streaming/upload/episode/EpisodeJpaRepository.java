package ma.streaming.upload.episode;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EpisodeJpaRepository extends JpaRepository<EpisodeEntity, UUID> {
    List<EpisodeEntity> findAllByDeletedAtIsNull(Pageable pageable);

    List<EpisodeEntity> findAllByDeletedAtIsNullAndSeason_Id(UUID seasonId);

    Optional<EpisodeEntity> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByTitleIgnoreCaseAndDeletedAtIsNull(String title);

    long countAllByDeletedAtIsNull();
}
