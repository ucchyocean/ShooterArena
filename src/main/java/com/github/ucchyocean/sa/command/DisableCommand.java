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
public class DisableCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "disable";
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

        boolean isForce = false;
        String name = args[1];

        if ( args[1].equalsIgnoreCase("force") && args.length >= 3 ) {
            isForce = true;
            name = args[2];
        }

        Arena arena = ArenaManager.getArena(name);

        if ( arena == null ) {
            sender.sendMessage(PREERR + "アリーナ" + name + "が存在しません。");
            return true;
        }

        SAGameSession session = ArenaManager.getSession(arena.getName());

        if ( session != null ) {
            if ( !isForce ) {
                sender.sendMessage(PREERR + "アリーナ" + name + "には、現在進行中のゲームが存在します。");
                sender.sendMessage(PREERR + "強制的に停止して、アリーナを準備中にするには、");
                sender.sendMessage(PREERR + "/sa disable force " + name + "  を実行してください。");
                return true;
            } else {
                session.cancelGame(); // ゲームを強制キャンセル
            }
        }

        // アリーナを準備中に変更する
        arena.setDisable();

        sender.sendMessage(PREINFO + "アリーナ" + name + "を準備中にしました。");
        return true;
    }

}
