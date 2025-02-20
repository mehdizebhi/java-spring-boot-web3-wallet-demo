package dev.mehdizebhi.web3.exceptions;

public class PasswordNotMatchException extends ApiBasicException {

    public PasswordNotMatchException(Throwable cause) {
        super(ErrorKind.NOT_MATCH_PASSWORD, cause);
    }

    public PasswordNotMatchException() {
        super(ErrorKind.NOT_MATCH_PASSWORD);
    }
}
