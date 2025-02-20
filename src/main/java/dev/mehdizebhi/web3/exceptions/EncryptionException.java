package dev.mehdizebhi.web3.exceptions;

public class EncryptionException extends ApiBasicException {
    public EncryptionException(Throwable cause) {
        super(ErrorKind.ENCRYPTION_FAILED, cause);
    }

    public EncryptionException() {
        super(ErrorKind.ENCRYPTION_FAILED);
    }
}
