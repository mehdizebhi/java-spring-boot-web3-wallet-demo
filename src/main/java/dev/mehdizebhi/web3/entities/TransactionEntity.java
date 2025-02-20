package dev.mehdizebhi.web3.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('transactions_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Size(max = 255)
    @NotNull
    @Column(name = "tx_hash", nullable = false)
    private String txHash;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 20, scale = 8)
    private BigDecimal amount;

    @NotNull
    @Column(name = "fee", nullable = false, precision = 20, scale = 8)
    private BigDecimal fee;

    @Size(max = 50)
    @NotNull
    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Size(max = 50)
    @NotNull
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @ColumnDefault("0")
    @Column(name = "confirmations")
    private Integer confirmations;

    @Size(max = 255)
    @Column(name = "block_hash")
    private String blockHash;

    @Column(name = "block_height")
    private Integer blockHeight;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}