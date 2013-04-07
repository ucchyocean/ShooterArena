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

import com.github.ucchyocean.sa.arena.Arena;
import com.github.ucchyocean.sa.arena.ArenaManager;
import com.github.ucchyocean.sa.game.GamePhase;
import com.github.ucchyocean.sa.game.SAGameSession;

/**
 * @author ucchy
 *
 */
public class ListenCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "listen";
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

        if ( args.length <= 1 ) {
            sender.sendMessage(PREERR + "アリーナ名を指定してください。");
            return true;
        }

        String name = args[1];

        if ( !ArenaManager.existArenaName(name) ) {
            sender.sendMessage(PREERR + "指定されたアリーナ名" + name + "が存在しません。");
            return true;
        }

        Arena arena = ArenaManager.getArena(name);
        if ( arena == null ) {
            sender.sendMessage(PREERR + "指定されたアリーナ名" + name + "が存在しません。");
            return true;
        }

        Location loc = arena.getRedRespawn();
        if ( loc == null ) {
            sender.sendMessage(PREERR + "指定されたアリーナ" + name + "に、リスポーン地点が設定されていません。");
            return true;
        }

        SAGameSession session = ArenaManager.getSession(name);
        if ( session == null ) {
            sender.sendMessage(PREERR + "指定されたアリーナ" + name + "は、現在ゲームが行われていません。");
            return true;
        }

        // 観客に追加
        session.addListener(player.getName());

        // 既にゲーム中なら、テレポート実行
        if ( session.phase == GamePhase.IN_GAME ) {
            player.teleport(loc, TeleportCause.PLUGIN);
        }

        return true;
    }

}
