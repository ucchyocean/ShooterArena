/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.sa.command;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.GameSession;

/**
 * @author ucchy
 *
 */
public class LeaveCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "leave";
    }

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#doCommand(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean doCommand(CommandSender sender, String[] args) {

        if ( !(sender instanceof Player) ) {
            sender.sendMessage(PREERR + "このコマンドはプレイヤーのみ実行可能です。");
            return true;
        }

        Player player = (Player)sender;
        GameSession session = ArenaManager.getSessionByPlayer(player);

        if ( session == null ) {
            sender.sendMessage(PREERR + "あなたはゲーム参加中のプレイヤーではありません。");
            return true;
        }

        // セッションから離脱する
        session.removePlayer(player.getName());

        // ゲーム中だったなら、ラウンジへテレポート
        if ( session.phase == GamePhase.IN_GAME ) {
            Location loc = ArenaManager.getLoungeRespawn();
            player.teleport(loc, TeleportCause.PLUGIN);
        }

        sender.sendMessage(PREINFO + "ゲームから離脱しました。");
        return true;
    }

}
