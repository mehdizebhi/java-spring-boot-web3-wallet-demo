package dev.mehdizebhi.web3.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorKind {
    NOT_FOUND_RESOURCE("Resource Not Found", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_BALANCE("Not Enough Balance", HttpStatus.NOT_ACCEPTABLE),
    NOT_FOUND_ENTITY("Resource Not Found", HttpStatus.NOT_FOUND),
    NOT_FOUND_USER("User Not Found", HttpStatus.NOT_FOUND),
    NOT_MATCH_PASSWORD("Password Not Match", HttpStatus.NOT_ACCEPTABLE),
    UNSUCCESSFUL_TRANSACTION("Unsuccessful Transaction", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_EXISTS_USER("User Already Exists", HttpStatus.CONFLICT),
    ALREADY_EXISTS_WALLET("Wallet Already Exists", HttpStatus.CONFLICT),
    INVALID_CRYPTOCURRENCY("Invalid Crypto Currency", HttpStatus.CONFLICT),
    INVALID_WALLET("Invalid Wallet", HttpStatus.CONFLICT),
    ENCRYPTION_FAILED("Encryption Failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private String message;
    private HttpStatusCode status;

    ErrorKind(String message, HttpStatusCode status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
