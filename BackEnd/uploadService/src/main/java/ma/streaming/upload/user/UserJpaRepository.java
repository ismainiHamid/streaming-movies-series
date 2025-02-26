package ma.streaming.upload.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findAllBy(Pageable pageable);

    List<UserEntity> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);

    Optional<UserEntity> findByEmailAndDisabledAtIsNull(String email);

    boolean existsByEmailAndDisabledAtNotNull(String email);

    boolean existsByEmailAndDisabledAtIsNull(String email);

    long countAllByEmailContainingIgnoreCase(String email);
}
