package dev.mehdizebhi.web3.controllers.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
public class TransactionDto {
    private Integer id;
    private String txHash;
    private BigDecimal amount;
    private BigDecimal fee;
    private String type;
    private String status;
}