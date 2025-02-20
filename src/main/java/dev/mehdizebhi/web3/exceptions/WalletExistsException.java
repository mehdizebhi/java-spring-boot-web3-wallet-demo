package dev.mehdizebhi.web3.exceptions;

public class WalletExistsException extends ApiBasicException {

  public WalletExistsException(Throwable cause) {
    super(ErrorKind.ALREADY_EXISTS_WALLET, cause);
  }

  public WalletExistsException() {
    super(ErrorKind.ALREADY_EXISTS_WALLET);
  }
}
