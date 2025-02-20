package dev.mehdizebhi.web3.exceptions;

public class InvalidCryptocurrency extends ApiBasicException {
    public InvalidCryptocurrency(Throwable cause) {
        super(ErrorKind.INVALID_CRYPTOCURRENCY, cause);
    }

    public InvalidCryptocurrency() {
        super(ErrorKind.INVALID_CRYPTOCURRENCY);
    }
}
