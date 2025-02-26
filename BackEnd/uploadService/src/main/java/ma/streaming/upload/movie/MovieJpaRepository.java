package ma.streaming.upload.movie;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieJpaRepository extends JpaRepository<MovieEntity, UUID> {
    List<MovieEntity> findAllByDeletedAtIsNull(Pageable pageable);

    List<MovieEntity> findByDeletedAtIsNullAndTitleContainingIgnoreCase(String title, Pageable pageable);

    Optional<MovieEntity> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByTitleIgnoreCaseAndDeletedAtIsNull(String title);

    boolean existsByTitleIgnoreCase(String title);

    long countByDeletedAtIsNullAndTitleContainingIgnoreCase(String title);

    long countAllByDeletedAtIsNull();
}
