package ma.streaming.upload.subtitle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubtitleJpaRepository extends JpaRepository<SubtitleEntity, UUID> {
}
