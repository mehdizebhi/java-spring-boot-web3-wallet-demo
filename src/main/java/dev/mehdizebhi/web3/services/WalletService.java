package dev.mehdizebhi.web3.services;

import dev.mehdizebhi.web3.constants.Cryptocurrency;
import dev.mehdizebhi.web3.constants.TransactionStatus;
import dev.mehdizebhi.web3.constants.TransactionType;
import dev.mehdizebhi.web3.entities.TransactionEntity;
import dev.mehdizebhi.web3.entities.UserEntity;
import dev.mehdizebhi.web3.entities.WalletEntity;
import dev.mehdizebhi.web3.exceptions.*;
import dev.mehdizebhi.web3.repositories.TransactionRepository;
import dev.mehdizebhi.web3.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.InsufficientMoneyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final BitcoinService bitcoinService;
    private final TransactionRepository transactionRepository;
    private final TransactionTemplate transactionTemplate;
    private final EncryptionService encryptionService;

    public WalletEntity createWallet(UserEntity userEntity, Cryptocurrency cryptocurrency) {
        if (!validateCreateWalletForUser(cryptocurrency, userEntity)) {
            throw new WalletExistsException();
        }
        WalletEntity walletEntity = switch (cryptocurrency) {
            case BITCOIN -> createBitcoinWallet(userEntity);
            default -> throw new InvalidCryptocurrency();
        };
        return walletRepository.save(walletEntity);
    }

    public Page<WalletEntity> wallets(UserEntity userEntity, Pageable pageable) {
        return walletRepository.findByUser(userEntity, pageable);
    }

    public WalletEntity wallet(UserEntity userEntity, Integer walletId) {
        return walletRepository.findByUserAndId(userEntity, walletId).orElseThrow(InvalidWalletException::new);
    }

    public Page<TransactionEntity> transactions(UserEntity userEntity, Integer walletId, Pageable pageable) {
        var wallet = wallet(userEntity, walletId);
        return transactionRepository.findByWallet(wallet, pageable);
    }

    public TransactionEntity sendTransaction(UserEntity userEntity, Integer walletId, String recipientAddress, BigDecimal amount) {
        var walletEntity = wallet(userEntity, walletId);
        if (validateBalanceForTransaction(walletEntity, amount)) {
            return switch (walletEntity.getCryptocurrency()) {
                case BITCOIN -> sendBitcoinTransaction(walletEntity, recipientAddress, amount);
                default -> throw new InvalidWalletException();
            };
        }
        throw new NotEnoughBalanceAvailable();
    }

    // ----------------------------
    // Private Helper
    // ----------------------------

    private boolean validateCreateWalletForUser(Cryptocurrency cryptocurrency, UserEntity userEntity) {
        return !walletRepository.existsByCryptocurrencyAndUser(cryptocurrency, userEntity);
    }

    private WalletEntity createBitcoinWallet(UserEntity userEntity) {
        var wallet = bitcoinService.createWallet();
        try {
            return WalletEntity.builder()
                    .user(userEntity)
                    .walletName("Bitcoin")
                    .cryptocurrency(Cryptocurrency.BITCOIN)
                    .publicAddress(wallet.currentReceiveAddress().toString())
                    .encryptedSeed(encryptionService.encrypt(wallet.getKeyChainSeed().getMnemonicString()))
                    .encryptedWalletData(bitcoinService.encryptWallet(wallet))
                    .availableBalance(BigDecimal.ZERO)
                    .unconfirmedBalance(BigDecimal.ZERO)
                    .build();
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private boolean validateBalanceForTransaction(WalletEntity walletEntity, BigDecimal amount) {
        return walletEntity.getAvailableBalance().compareTo(amount) >= 0;
    }

    private TransactionEntity sendBitcoinTransaction(final WalletEntity walletEntity, String recipientAddress, BigDecimal amount) {
        var btcWallet = bitcoinService.loadWallet(walletEntity.getEncryptedWalletData());
        var tx = transactionTemplate.execute(status -> {
            walletEntity.setAvailableBalance(walletEntity.getAvailableBalance().subtract(amount));
            var sendTx = bitcoinService.sendTransaction(btcWallet, recipientAddress, amount);
            walletRepository.save(walletEntity);
            if (sendTx != null) {
                return sendTx;
            } else {
                throw new SendTransactionException();
            }
        });

        try {
            walletEntity.setEncryptedWalletData(bitcoinService.encryptWallet(btcWallet));
            walletRepository.save(walletEntity);
        } catch (EncryptWalletException e) {
            log.error("Error with encrypt bitcoin wallet", e);
        }

        assert tx != null;
        tx.getConfidence().addEventListener((confidence, reason) -> {
            if (confidence.getDepthInBlocks() >= 1) {
                transactionRepository.findByTxHash(tx.getTxId().toString())
                        .ifPresent(existingTx -> {
                            if (!existingTx.getStatus().equals(TransactionStatus.CONFIRMED)) {
                                existingTx.setStatus(TransactionStatus.CONFIRMED);
                                transactionRepository.save(existingTx);
                            }
                        });
            }
        });
        var transactionEntity = TransactionEntity.builder()
                .wallet(walletEntity)
                .amount(amount)
                .fee(tx.getFee().toBtc())
                .transactionType(TransactionType.OUTGOING)
                .status(TransactionStatus.PENDING)
                .txHash(tx.getTxId().toString())
                .build();

        return transactionRepository.save(transactionEntity);
    }
}