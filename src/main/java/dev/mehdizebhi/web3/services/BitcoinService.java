package dev.mehdizebhi.web3.services;

import dev.mehdizebhi.web3.exceptions.EncryptWalletException;
import dev.mehdizebhi.web3.exceptions.LoadWalletException;
import dev.mehdizebhi.web3.exceptions.NotEnoughBalanceAvailable;
import dev.mehdizebhi.web3.utils.BitcoinUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.bitcoinj.core.*;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BitcoinService {

    private final NetworkParameters params;
    private final PeerGroup peerGroup;
    private final EncryptionService encryptionService;

    public Wallet createWallet() {
        var wallet = Wallet.createDeterministic(new Context(params), Script.ScriptType.P2PKH);
        peerGroup.addWallet(wallet);
        return wallet;
    }

    public Wallet loadWallet(@NotNull String encryptedWalletData) throws LoadWalletException {
        try {
            String decryptedWalletData = encryptionService.decrypt(encryptedWalletData);
            return BitcoinUtils.deserializeWallet(decryptedWalletData);
        } catch (Exception e) {
            throw new LoadWalletException("Failed to load wallet", e);
        }
    }

    public String encryptWallet(@NotNull Wallet wallet) throws EncryptWalletException {
        try {
            String serializedWallet = BitcoinUtils.serializeWallet(wallet);
            return encryptionService.encrypt(serializedWallet);
        } catch (Exception e) {
            throw new EncryptWalletException("Failed to encrypt wallet", e);
        }
    }

    public Transaction sendTransaction(@NotNull final Wallet wallet, @NotNull String recipientAddress, BigDecimal amount) {
        try {
            Address forwardingAddress = Address.fromString(params, recipientAddress);
            final Coin value = Coin.parseCoin(amount.toString());
            final Coin amountToSend = value.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
            Wallet.SendResult result = wallet.sendCoins(peerGroup, forwardingAddress, amountToSend);
            return result.tx;
        } catch (InsufficientMoneyException e) {
            throw new NotEnoughBalanceAvailable(e);
        }
    }
}
