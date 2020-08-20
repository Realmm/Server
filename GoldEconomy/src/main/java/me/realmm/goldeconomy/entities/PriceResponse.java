package me.realmm.goldeconomy.entities;

import java.math.BigDecimal;

public enum PriceResponse {

    SUCCESS,
    FAILED;

    private BigDecimal price;

    public PriceResponse setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
