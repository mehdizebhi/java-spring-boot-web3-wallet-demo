package dev.mehdizebhi.web3.listeners;

import dev.mehdizebhi.web3.constants.TransactionStatus;
import dev.mehdizebhi.web3.constants.TransactionType;
import dev.mehdizebhi.web3.entities.TransactionEntity;
import dev.mehdizebhi.web3.repositories.TransactionRepository;
import dev.mehdizebhi.web3.repositories.WalletRepository;
import dev.mehdizebhi.web3.services.BitcoinService;
import dev.mehdizebhi.web3.utils.BitcoinUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class BitcoinNetworkListener {

    private final PeerGroup peerGroup;
    private final NetworkParameters params;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionTemplate transactionTemplate;
    private final BitcoinService bitcoinService;

    @PostConstruct
    public void init() {
        walletRepository.findAll().forEach(wallet -> {
            peerGroup.addWallet(bitcoinService.loadWallet(wallet.getEncryptedWalletData()));
        });
        peerGroup.addOnTransactionBroadcastListener(
                (peer, t) -> handleIncomingTransaction(t)
        );
    }

    private void handleIncomingTransaction(Transaction transaction) {
        log.info("handle incoming transaction");
        transactionTemplate.executeWithoutResult(status -> {
            int depth = transaction.getConfidence().getDepthInBlocks();
            boolean confirmed = depth > 0;

            BigDecimal feeValue = transaction.getFee() != null ? transaction.getFee().toBtc()
                    : BigDecimal.ZERO;

            try {
                for (TransactionOutput output : transaction.getOutputs()) {

                    // Address address = output.getScriptPubKey().getToAddress(params);
                    Address address = BitcoinUtils.getOutputAddress(output, params);
                    if (address == null) {
                        return;
                    }
                    String addressStr = address.toString();
                    var wallets = walletRepository.findByPublicAddress(addressStr);
                    if (wallets != null && !wallets.isEmpty()) {
                        BigDecimal amount = new BigDecimal(output.getValue().getValue()).movePointLeft(8);

                        for (var wallet : wallets) {
                            var existTxEntityOpt = transactionRepository.findByTxHash(transaction.getTxId().toString());
                            if (existTxEntityOpt.isPresent()) {
                                var existTxEntity = existTxEntityOpt.get();
                                if (confirmed && existTxEntity.getStatus().equals(TransactionStatus.UNCONFIRMED)) {
                                    wallet.setAvailableBalance(wallet.getAvailableBalance().add(amount));
                                    wallet.setUnconfirmedBalance(wallet.getUnconfirmedBalance().subtract(amount));
                                    existTxEntity.setStatus(TransactionStatus.CONFIRMED);
                                    walletRepository.save(wallet);
                                    transactionRepository.save(existTxEntity);
                                }
                            } else {
                                if (confirmed) {
                                    wallet.setAvailableBalance(wallet.getAvailableBalance().add(amount));
                                } else {
                                    wallet.setUnconfirmedBalance(wallet.getUnconfirmedBalance().add(amount));
                                }
                                var newTxEntity = TransactionEntity.builder()
                                        .wallet(wallet)
                                        .txHash(transaction.getTxId().toString())
                                        .amount(amount)
                                        .fee(feeValue)
                                        .transactionType(TransactionType.INCOMING)
                                        .status(confirmed ? TransactionStatus.CONFIRMED : TransactionStatus.UNCONFIRMED)
                                        .confirmations(depth)
                                        .build();
                                walletRepository.save(wallet);
                                transactionRepository.save(newTxEntity);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Error processing transaction output: ", e);
            }
        });
    }
}