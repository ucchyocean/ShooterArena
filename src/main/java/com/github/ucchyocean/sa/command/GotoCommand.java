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

/**
 * @author ucchy
 *
 */
public class GotoCommand extends CommandAbst {

    /**
     * @see com.github.ucchyocean.sa.command.CommandAbst#getCommand()
     */
    @Override
    public String getCommand() {
        return "goto";
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

        if ( !name.equalsIgnoreCase("lounge") && !ArenaManager.existArenaName(name) ) {
            sender.sendMessage(PREERR + "指定されたアリーナ名" + name + "が存在しません。");
            return true;
        }

        Location loc = null;
        if ( name.equalsIgnoreCase("lounge") ) {
            loc = ArenaManager.getLoungeRespawn();
        } else {
            Arena arena = ArenaManager.getArena(name);
            loc = arena.getRedRespawn();
        }

        if ( loc == null ) {
            sender.sendMessage(PREERR + "指定されたアリーナ" + name + "に、リスポーン地点が設定されていません。");
            return true;
        }

        // テレポート実行
        player.teleport(loc, TeleportCause.PLUGIN);
        sender.sendMessage(PREINFO + "アリーナ" + name + "にテレポートしました。");

        return true;
    }

}
