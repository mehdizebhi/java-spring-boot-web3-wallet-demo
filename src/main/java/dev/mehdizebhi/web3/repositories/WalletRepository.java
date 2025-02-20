package dev.mehdizebhi.web3.repositories;

import dev.mehdizebhi.web3.constants.Cryptocurrency;
import dev.mehdizebhi.web3.entities.UserEntity;
import dev.mehdizebhi.web3.entities.WalletEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<WalletEntity, Integer> {

    boolean existsByCryptocurrencyAndUser(Cryptocurrency cryptocurrency, UserEntity userEntity);

    List<WalletEntity> findByPublicAddress(@Size(max = 255) @NotNull String publicAddress);

    Page<WalletEntity> findByUser(@NotNull UserEntity user, Pageable pageable);

    Optional<WalletEntity> findByUserAndId(@NotNull UserEntity user, Integer id);
}