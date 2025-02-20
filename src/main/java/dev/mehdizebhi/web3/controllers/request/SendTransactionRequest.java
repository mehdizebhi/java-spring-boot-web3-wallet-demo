package dev.mehdizebhi.web3.controllers.request;

import java.math.BigDecimal;

public record SendTransactionRequest(String recipientAddress, BigDecimal amount) {
}