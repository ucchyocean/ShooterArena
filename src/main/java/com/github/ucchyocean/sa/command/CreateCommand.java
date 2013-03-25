/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.command.CommandSender;

import com.github.ucchyocean.sa.arena.ArenaManager;

/**
 * @author ucchy
 *
 */
public class CreateCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "create";
    }

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#doCommand(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean doCommand(CommandSender sender, String[] args) {

        if ( args.length <= 1 ) {
            sender.sendMessage(PREERR + "アリーナ名を指定してください。");
            return true;
        }

        String name = args[1];

        if ( ArenaManager.existArenaName(name) ) {
            sender.sendMessage(PREERR + "指定されたアリーナ名" + name + "は既に存在します。");
            return true;
        }

        if ( !ArenaManager.existArenaRegion(name) ) {
            sender.sendMessage(PREERR + "指定されたアリーナ名" + name + "に領域がありません。");
            sender.sendMessage(PREERR + "先にWorldGuardで、領域" + name + "を登録してください。");
            return true;
        }

        // 登録実行
        ArenaManager.registerNewArena(name);
        sender.sendMessage(PREINFO + "アリーナ" + name + "を新規登録しました。");

        return true;
    }

}
