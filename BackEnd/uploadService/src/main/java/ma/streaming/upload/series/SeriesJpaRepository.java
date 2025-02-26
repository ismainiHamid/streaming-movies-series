package ma.streaming.upload.series;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeriesJpaRepository extends JpaRepository<SeriesEntity, UUID> {
    List<SeriesEntity> findAllByDeletedAtIsNull(Pageable pageable);

    List<SeriesEntity> findAllByDeletedAtIsNullAndTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<SeriesEntity> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByTitleIgnoreCaseAndDeletedAtIsNull(String title);

    long countAllByDeletedAtIsNullAndTitleContainingIgnoreCase(String title);

    long countAllByDeletedAtIsNull();
}
