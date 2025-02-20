package dev.mehdizebhi.web3.entities;

import dev.mehdizebhi.web3.constants.Cryptocurrency;
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

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "wallets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('wallets_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 255)
    @Column(name = "wallet_name")
    private String walletName;

    @Size(max = 255)
    @NotNull
    @Column(name = "public_address", nullable = false)
    private String publicAddress;

    @Column(name = "encrypted_seed", length = Integer.MAX_VALUE)
    private String encryptedSeed;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToOne
    private Balance balance;

    @OneToMany(mappedBy = "wallet")
    private Set<Transaction> transactions = new LinkedHashSet<>();

    @Size(max = 255)
    @NotNull
    @Column(name = "cryptocurrency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Cryptocurrency cryptocurrency;

}