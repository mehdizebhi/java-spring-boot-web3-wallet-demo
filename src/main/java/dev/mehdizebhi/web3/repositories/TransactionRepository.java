package dev.mehdizebhi.web3.repositories;

import dev.mehdizebhi.web3.entities.TransactionEntity;
import dev.mehdizebhi.web3.entities.WalletEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    Page<TransactionEntity> findByWallet(@NotNull WalletEntity wallet, Pageable pageable);

    Optional<TransactionEntity> findByTxHash(@Size(max = 255) @NotNull String txHash);
}