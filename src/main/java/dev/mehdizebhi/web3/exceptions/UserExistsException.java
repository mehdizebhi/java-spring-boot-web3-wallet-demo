package dev.mehdizebhi.web3.exceptions;

public class UserExistsException extends ApiBasicException {

    public UserExistsException(Throwable cause) {
        super(ErrorKind.ALREADY_EXISTS_USER, cause);
    }

    public UserExistsException() {
        super(ErrorKind.ALREADY_EXISTS_USER);
    }
}
