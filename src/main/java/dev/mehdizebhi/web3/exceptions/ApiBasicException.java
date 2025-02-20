package dev.mehdizebhi.web3.exceptions;

import org.springframework.web.ErrorResponseException;

public class ApiBasicException extends ErrorResponseException {

    private ErrorKind kind;

    public ApiBasicException(ErrorKind kind, Throwable cause) {
        super(kind.getStatus(), cause);
        this.kind = kind;
    }

    public ApiBasicException(ErrorKind kind) {
        super(kind.getStatus());
        this.kind = kind;
    }

    public ErrorKind getKind() {
        return kind;
    }
}