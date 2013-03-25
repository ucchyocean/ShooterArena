/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 * @author ucchy
 * コマンドの抽象クラス
 */
public abstract class CommandAbst {

    protected String PREERR = ChatColor.RED + "[SA]";
    protected String PREINFO = ChatColor.AQUA + "[SA]";
    protected String PRENOTICE = ChatColor.LIGHT_PURPLE + "[SA]";

    /**
     * コマンド識別子を返す
     * @return コマンド識別子
     */
    public abstract String getCommand();

    /**
     * コマンドが実行されたときに呼び出されるメソッド
     * @param sender コマンド実行者
     * @param args コマンドの引数
     * @return
     */
    public abstract boolean doCommand(CommandSender sender, String[] args);

    /**
     * Locationの座標を、文字列表現に変換して返します。
     * @param location 座標
     * @return 文字列表現
     */
    protected String getBlockPointDesc(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        return String.format("(%d, %d, %d)", x, y, z);
    }
}
