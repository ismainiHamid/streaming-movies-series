package ma.streaming.upload.media;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MediaJpaRepository extends JpaRepository<MediaEntity, UUID> {
    List<MediaEntity> findAllByDeletedAtIsNullAndTitleIsContainingIgnoreCase(String title, Pageable pageable);

    List<MediaEntity> findAllByGenres_NameContainingIgnoreCase(String genres);

    Optional<MediaEntity> findByIdAndDeletedAtIsNull(UUID id);

    long countAllByDeletedAtIsNullAndTitleIsContainingIgnoreCase(String title);
}
