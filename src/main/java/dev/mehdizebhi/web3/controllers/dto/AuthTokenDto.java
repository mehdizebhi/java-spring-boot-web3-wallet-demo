package dev.mehdizebhi.web3.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@AllArgsConstructor
@Getter
@Setter
public class AuthTokenDto {
    private String token;
    private String type;
    private Long expires;
}
