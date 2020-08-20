package me.realmm.goldeconomy.entities;

import net.jamesandrew.commons.number.Number;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Item {

    private final Material m;
    private int amount;
    private final short data;
    private BigDecimal cost;
    private final String split = "@";
    private final EconPlayer ep;
    private String name;

    public Item(EconPlayer ep, Material m, int amount, short data, double cost) {
        this(ep, m, amount, data, BigDecimal.valueOf(cost));
    }

    public Item(EconPlayer ep, String serialized) {
        String[] a = serialized.split(split);
        if (a.length != 4 && a.length != 5) throw new IllegalArgumentException("Unable to generate item from serialized string, length of " + a.length);
        Material m = Material.matchMaterial(a[0]);
        int amount = Number.getInt(a[1]);
        short data = Number.getShort(a[2]);

        if (a.length == 5) {
            name = a[3];
            cost = BigDecimal.valueOf(Number.getDouble(a[4]));
        } else {
            cost = BigDecimal.valueOf(Number.getDouble(a[3]));
        }

        this.m = m;
        this.amount = amount;
        this.data = data;
        this.ep = ep;
    }

    public Item(EconPlayer ep, Material m, int amount, short data, BigDecimal cost) {
        this.m = m;
        this.ep = ep;
        this.amount = amount;
        this.data = data;
        this.cost = cost;
    }

    public Material getType() {
        return m;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount, boolean changeCost) {
        if (changeCost) cost = getIndividualCost().multiply(BigDecimal.valueOf(amount));
        this.amount = amount;
    }

    public void setAmount(int amount) {
        setAmount(amount, true);
    }

    public boolean hasEconPlayer() {
        return ep != null;
    }

    public EconPlayer getEconPlayer() {
        return ep;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public boolean hasName() {
        return name != null && !name.equals("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIndividualCost() {
        return cost.divide(BigDecimal.valueOf(amount), 20, RoundingMode.HALF_EVEN);
    }

    public short getData() {
        return data;
    }

    public boolean equals(Item item) {
        if (item == this) return true;
        boolean samePlayer = item.getEconPlayer() == null || (getEconPlayer() != null && item.getEconPlayer().getOfflinePlayer().getUniqueId().equals(getEconPlayer().getOfflinePlayer().getUniqueId()));
        return item.getAmount() == getAmount() && item.getType() == getType() && item.getIndividualCost().compareTo(getIndividualCost()) == 0 && item.getData() == getData() && samePlayer;
    }

    public boolean isSimilar(Item item) {
        if (equals(item)) return true;
        boolean samePlayer = item.getEconPlayer() == null || (getEconPlayer() != null && item.getEconPlayer().getOfflinePlayer().getUniqueId().equals(getEconPlayer().getOfflinePlayer().getUniqueId()));
        return item.getType() == getType() && item.getIndividualCost().compareTo(getIndividualCost()) == 0 && item.getData() == getData() && samePlayer;
    }

    public Item clone() {
        return new Item(ep, m, amount, data, cost);
    }

    public String serialize() {
        return m.name() + split + amount + split + data + split + (hasName() ? getName() + split : "") + cost.doubleValue();
    }

}
