package dev.mehdizebhi.web3.exceptions;

public class UserNotFoundException extends ApiBasicException {

    public UserNotFoundException(Throwable cause) {
        super(ErrorKind.NOT_FOUND_USER, cause);
    }

    public UserNotFoundException() {
        super(ErrorKind.NOT_FOUND_USER);
    }
}