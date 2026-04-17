package com.backend.trackinvest.usuarios.domain.wallet.models.valueobjects;

public enum CurrencyTypeEnum {
    USD("USD"),
    EUR("EUR"),
    COP("COP"),
    GBP("GBP"),
    JPY("JPY"),
    AUD("AUD"),
    CAD("CAD"),
    CHF("CHF"),
    CNY("CNY"),
    SEK("SEK"),
    NZD("NZD");

    private final String code;

    CurrencyTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
