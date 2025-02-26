package ma.streaming.upload.actor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActorJpaRepository extends JpaRepository<ActorEntity, UUID> {
    List<ActorEntity> findAllByOrderByNameAsc();

    List<ActorEntity> findAllBy(Pageable pageable);

    List<ActorEntity> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
