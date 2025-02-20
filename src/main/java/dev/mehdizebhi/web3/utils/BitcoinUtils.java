package dev.mehdizebhi.web3.utils;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptPattern;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class BitcoinUtils {

    public static String serializeWallet(Wallet wallet) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wallet.saveToFileStream(baos);
        byte[] walletBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(walletBytes);
    }

    public static Wallet deserializeWallet(String serializedWallet) throws UnreadableWalletException {
        byte[] walletBytes = Base64.getDecoder().decode(serializedWallet);
        ByteArrayInputStream bais = new ByteArrayInputStream(walletBytes);
        return Wallet.loadFromFileStream(bais, null);
    }

    public static Address getOutputAddress(TransactionOutput output, NetworkParameters params) {
        Script script = output.getScriptPubKey();
        if (ScriptPattern.isP2PKH(script) || ScriptPattern.isP2SH(script)) {
            return script.getToAddress(params, true);
        } else {
            return null;
        }
    }
}
