package dev.mehdizebhi.web3.exceptions;

public class NotEnoughBalanceAvailable extends ApiBasicException {

    public NotEnoughBalanceAvailable(Throwable cause) {
        super(ErrorKind.NOT_ENOUGH_BALANCE, cause);
    }

    public NotEnoughBalanceAvailable() {
        super(ErrorKind.NOT_ENOUGH_BALANCE);
    }
}