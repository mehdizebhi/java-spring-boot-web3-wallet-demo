package dev.mehdizebhi.web3.exceptions;

public class NotFoundEntity extends ApiBasicException {
    public NotFoundEntity(Throwable cause) {
        super(ErrorKind.NOT_FOUND_ENTITY, cause);
    }

    public NotFoundEntity() {
        super(ErrorKind.NOT_FOUND_ENTITY);
    }
}
