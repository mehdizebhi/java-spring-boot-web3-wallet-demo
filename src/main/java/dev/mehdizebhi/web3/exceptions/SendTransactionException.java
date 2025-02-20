package dev.mehdizebhi.web3.exceptions;

public class SendTransactionException extends ApiBasicException {

    public SendTransactionException(Throwable cause) {
        super(ErrorKind.UNSUCCESSFUL_TRANSACTION, cause);
    }

    public SendTransactionException() {
        super(ErrorKind.UNSUCCESSFUL_TRANSACTION);
    }
}