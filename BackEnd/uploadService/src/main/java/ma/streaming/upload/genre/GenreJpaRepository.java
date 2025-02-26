package ma.streaming.upload.genre;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenreJpaRepository extends JpaRepository<GenreEntity, UUID> {
    List<GenreEntity> findAllByOrderByNameAsc();

    List<GenreEntity> findAllBy(Pageable pageable);

    List<GenreEntity> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
