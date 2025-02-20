package dev.mehdizebhi.web3.repositories;

import dev.mehdizebhi.web3.entities.UserEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u, UserSession us WHERE u.id = us.user.id AND us.token = :token AND us.tokenExpireAt > :time AND us.tokenRevokedAt is null")
    Optional<UserEntity> findByToken(@Param("token") String token, @Param("time") Instant time);

    Optional<UserEntity> findByUsername(@Size(max = 255) @NotNull String username);

    boolean existsByUsernameOrEmail(@Size(max = 255) @NotNull String username, @Size(max = 255) @NotNull String email);
}