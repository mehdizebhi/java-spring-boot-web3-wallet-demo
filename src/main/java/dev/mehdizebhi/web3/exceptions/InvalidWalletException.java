package dev.mehdizebhi.web3.exceptions;

public class InvalidWalletException extends ApiBasicException {

    public InvalidWalletException(Throwable cause) {
        super(ErrorKind.INVALID_WALLET, cause);
    }

    public InvalidWalletException() {
        super(ErrorKind.INVALID_WALLET);
    }
}