package dev.mehdizebhi.web3.entities;

import dev.mehdizebhi.web3.constants.Cryptocurrency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "wallets")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('wallets_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Size(max = 255)
    @Column(name = "wallet_name")
    private String walletName;

    @Size(max = 255)
    @NotNull
    @Column(name = "public_address", nullable = false)
    private String publicAddress;

    @Column(name = "encrypted_seed", length = Integer.MAX_VALUE)
    private String encryptedSeed;

    @Column(name = "encrypted_wallet_data", length = Integer.MAX_VALUE)
    private String encryptedWalletData;

    @OneToMany(mappedBy = "wallet", cascade = {CascadeType.PERSIST})
    private Set<TransactionEntity> transactionEntities = new LinkedHashSet<>();

    @NotNull
    @Column(name = "cryptocurrency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Cryptocurrency cryptocurrency;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "available_balance", nullable = false, precision = 20, scale = 8)
    private BigDecimal availableBalance;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "unconfirmed_balance", nullable = false, precision = 20, scale = 8)
    private BigDecimal unconfirmedBalance;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public void addTransaction(TransactionEntity transactionEntity) {
        transactionEntities.add(transactionEntity);
    }

}