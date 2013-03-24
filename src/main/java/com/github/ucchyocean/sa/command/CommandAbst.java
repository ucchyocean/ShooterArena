/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.command.CommandSender;

/**
 * @author ucchy
 * コマンドの抽象クラス
 */
public abstract class CommandAbst {

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
}
