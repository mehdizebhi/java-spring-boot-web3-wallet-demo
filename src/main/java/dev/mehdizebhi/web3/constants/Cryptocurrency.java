package dev.mehdizebhi.web3.constants;

import dev.mehdizebhi.web3.exceptions.InvalidCryptocurrency;

public enum Cryptocurrency {
    BITCOIN;

    public static Cryptocurrency fromString(String str) {
        for (Cryptocurrency cryptocurrency : Cryptocurrency.values()) {
            if (cryptocurrency.toString().equalsIgnoreCase(str)) {
                return cryptocurrency;
            }
        }
        throw new InvalidCryptocurrency();
    }
}
