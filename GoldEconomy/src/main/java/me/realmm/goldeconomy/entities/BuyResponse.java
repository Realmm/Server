package me.realmm.goldeconomy.entities;

import net.jamesandrew.realmlib.placeholder.ReplacePattern;

public enum BuyResponse {

    FULL_INVENTORY,
    NOT_ENOUGH_ITEMS,
    INCREASE_LIMIT,
    INSUFFICIENT_BALANCE,
    BOUGHT_ITEM;

    private ReplacePattern pattern;

    BuyResponse setPattern(ReplacePattern pattern) {
        this.pattern = pattern;
        return this;
    }

    public boolean hasPattern() {
        return pattern != null;
    }

    public ReplacePattern getPattern() {
        return pattern;
    }

}
