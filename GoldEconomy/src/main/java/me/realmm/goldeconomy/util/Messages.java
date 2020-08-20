package me.realmm.goldeconomy.util;

import me.realmm.goldeconomy.GoldEconomy;
import net.jamesandrew.realmlib.lang.Lang;
import org.bukkit.plugin.java.JavaPlugin;

public final class Messages {

    public static final String SCOREBOARD_BALANCE = get("scoreboard-balance");

    public static final String BALTOP_TITLE = get("baltop-title");
    public static final String BALTOP_LINE = get("baltop-line");

    public static final String PUT_ITEM_ON_MARKET = get("put-item-on-market");
    public static final String INCORRECT_ARGUMENTS = get("incorrect-arguments");
    public static final String NOT_ENOUGH_ITEMS = get("not-enough-items");
    public static final String INSUFFICIENT_BALANCE = get("insufficient-balance");
    public static final String FULL_INVENTORY = get("full-inventory");
    public static final String SOLD_ITEM = get("sold-item");
    public static final String BOUGHT_ITEM = get("bought-item");
    public static final String DEPOSITED = get("deposited");
    public static final String WITHDRAWN = get("withdrawn");
    public static final String BALANCE = get("balance");
    public static final String INCREASE_LIMIT = get("increase-limit");
    public static final String PRICE = get("price");
    public static final String NOT_ENOUGH_GOLD_INVENTORY = get("not-enough-gold-inventory");
    public static final String NOT_ENOUGH_SELL = get("not-enough-sell");
    public static final String ILLEGAL_MARKET_ITEM = get("illegal-market-item");
    public static final String BALANCE_PLAYER = get("balance-player");

    private static String get(String path) {
        return Lang.color(JavaPlugin.getPlugin(GoldEconomy.class).getMessages().getConfig().getString(path));
    }

}
