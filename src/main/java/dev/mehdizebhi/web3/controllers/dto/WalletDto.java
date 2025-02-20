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
public class WalletDto {
    private Integer id;
    private String currency;
    private String publicAddress;
    private BigDecimal availableBalance;
    private BigDecimal unconfirmedBalance;
}
