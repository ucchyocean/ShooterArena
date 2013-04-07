/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.github.ucchyocean.sa.ShooterArena;

/**
 * @author ucchy
 * コマンドの抽象クラス
 */
public abstract class CommandAbst {

    protected String PREERR = ShooterArena.PREERR;
    protected String PREINFO = ShooterArena.PREINFO;
    protected String PRENOTICE = ShooterArena.PRENOTICE;

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
