/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.command.CommandSender;

import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 *
 */
public class RemoveCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "remove";
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
        Arena arena = ArenaManager.getArena(name);

        if ( arena == null ) {
            sender.sendMessage(PREERR + "アリーナ" + name + "が存在しません。");
            return true;
        }

        if ( arena.getMode() != null ) {
            sender.sendMessage(PREERR + "アリーナ" + name + "が準備中になっていません。");
            return true;
        }

        SAGameSession session = arena.getSession();

        if ( session != null ) {
            sender.sendMessage(PREERR + "アリーナ" + name + "にゲームが存在します。");
            return true;
        }

        // アリーナを削除する
        ArenaManager.removeArena(name);

        sender.sendMessage(PREINFO + "アリーナ" + name + "を削除しました。");
        return true;
    }

}
